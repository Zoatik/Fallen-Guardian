
import java.awt.event.MouseEvent
import scala.collection.mutable

/**
 * 2D Box representation
 * @param x       absolute pos x
 * @param y       absolute pos y
 * @param width   width of the 2D box
 * @param height  height of the 2D box
 */
case class Box(x: Int, y: Int, width: Int, height: Int) {

  def intersects(other: Box): Boolean = {
    this.x < other.x + other.width  &&
    this.x + this.width > other.x   &&
    this.y < other.y + other.height &&
    this.height + this.y > other.y
  }
  /**
   * Check if a given point is inside the 2D Box
   * @param px  point absolute X
   * @param py  point absolute Y
   * @return true if the point is in the 2D Box - false otherwise
   */
  def containsPoint(px: Int, py: Int): Boolean = {

    px >= x  && px < x + width && py >= y && py < y + height

  }
}

/**
 * Collision Area with mouse Events
 * @param id          unique id of the Collision Area
 * @param initialBox  shape of the Area
 * @note Should not be instanciated directly -> use CollisionBox2DManager
 */
class CollisionBox2D (val id: String, initialBox: Box) {
  private var box: Box = initialBox
  private val mouseEnterListeners: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()
  private val mouseLeaveListeners: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()
  private val mousePressedListeners: mutable.ListBuffer[Int => Unit] = mutable.ListBuffer()
  private val mouseReleasedListeners: mutable.ListBuffer[Int => Unit] = mutable.ListBuffer()

  var isMouseOver: Boolean = false


  def collidesWith(other: CollisionBox2D): Boolean = this.box.intersects(other.box)

  /**
   * Sets the absolute position of the box
   * @param newX New absolute position X
   * @param newY New absolute position Y
   */
  def setPosition(newX: Int, newY: Int): Unit = {
    box = box.copy(x = newX, y = newY)
  }

  def setSize(newWidth: Int, newHeight: Int): Unit = {
    box = box.copy(width = newWidth, height = newHeight)
  }

  /**
   * Binds a function to the mouse enter event
   * @param listener function to trigger
   */
  def onMouseEnter(listener: () => Unit): Unit = {
    mouseEnterListeners += listener
  }

  /**
   * Binds a function to the mouse leave event
   * @param listener function to trigger
   */
  def onMouseLeave(listener: () => Unit): Unit = {
    mouseLeaveListeners += listener
  }

  /**
   * Binds a function to the mouse pressed event
   * @param listener function to trigger
   */
  def onMousePressed(listener: Int => Unit): Unit = {
    mousePressedListeners += listener
  }

  /**
   * Binds a function to the mouse released event
   * @param listener function to trigger
   */
  def onMouseReleased(listener: Int => Unit): Unit = {
    mouseReleasedListeners += listener
  }

  /**
   * Checks if the mouse is on the Collision Area
   *
   * Should only be called by the Collision Manager
   * @param mouseX absolute X mouse position
   * @param mouseY absolute Y mouse position
   * @return true if collision - false otherwise
   */
  def checkMouseCollision(mouseX: Int, mouseY: Int): Boolean = {
    if (box.containsPoint(mouseX, mouseY) && !isMouseOver) {
      isMouseOver = true
      mouseEnterListeners.foreach(_())
      return true
    }
    else if(!box.containsPoint(mouseX, mouseY) && isMouseOver){
      isMouseOver = false
      mouseLeaveListeners.foreach(_())
    }
    false
  }

  def mouseLeave(): Unit = {
    isMouseOver = false
    mouseLeaveListeners.foreach(_())
  }

  /**
   * Calls all listeners when mouse is pressed
   * @return true if the event is fired - false otherwise
   */
  def mousePressed(mouseButton: Int): Boolean = {
    if(!isMouseOver)
      return false
    mousePressedListeners.foreach(_(mouseButton))
    true
  }

  /**
   * Calls all listeners when mouse is released
   * @return true if the event is fired - false otherwise
   */
  def mouseReleased(mouseButton: Int): Boolean = {
    if(!isMouseOver)
      return false
    mouseReleasedListeners.foreach(_(mouseButton))
    true
  }

  /**
   * returns the Box shape
   * @return box shape : Box
   */
  def getBox: Box = box
}

/**
 * Collision Manager object - handles all collision detections when specific events are fired
 */
object CollisionBox2DManager {
  private val layers: Array[mutable.ListBuffer[CollisionBox2D]] =
                        Array.fill(Constants.NUMBER_OF_LAYERS)(mutable.ListBuffer[CollisionBox2D]())
  private var boxesCounter: Int = 0
  private var prevTime: Long = 0
  private var isMouseDown: Boolean = false

  InputManager.bindMouseMotion((x,y) => checkMouseCollisions(x,y))
  InputManager.bindMouseButton(MouseEvent.BUTTON1, (mouseButton, pressed) => handleMouseAction(mouseButton, pressed))
  InputManager.bindMouseButton(MouseEvent.BUTTON3, (mouseButton, pressed) => handleMouseAction(mouseButton, pressed))


  def destroy(collisionBox2D: CollisionBox2D): Unit = {
    val targetLayer = layers.find(layer => layer.contains(collisionBox2D)).getOrElse(return)
    collisionBox2D.mouseLeave()
    targetLayer -= collisionBox2D
  }
  /**
  * Adds a new collision Area to the Manager
  * @param initialBox  Box shape
  * @param layer       Collision layer height - 0 if empty
  * @return new Collision Area : collisionBox2D
  */
  def newCollisionBox2D(initialBox: Box, layer: Int = 0): CollisionBox2D = {
    require(layer >= 0 && layer < layers.length, s"Invalid layer: $layer")
    val newBox = new CollisionBox2D(s"Box$boxesCounter", initialBox)
    register(newBox, layer)
    newBox
  }

  /**
  * Adds a new Collision Area to a specific layer
  * @param box   Collision Area: CollisionBox2D
  * @param layer Collision layer height
  */
  def register(box: CollisionBox2D, layer: Int): Unit = {
    require(layer >= 0 && layer < layers.length, s"Invalid layer: $layer")
    layers(layer) += box
    boxesCounter += 1
  }

  /**
  * Removes the given Collision Area from a layer
  * @param box   CollisionArea to remove
  * @param layer targeted layer height
  */
  def unregister(box: CollisionBox2D, layer: Int): Unit = {
    require(layer >= 0 && layer < layers.length, s"Invalid layer: $layer")
    layers(layer) -= box
  }

  /**
  * Checks mouse collision with the first heighest Collision layer
  *
  * Triggers events when collision found
  * @param mouseX  Absolute mouse position X
  * @param mouseY  Absolute mouse position Y
  */
  def checkMouseCollisions(mouseX: Int, mouseY: Int): Unit = {
    if(GameManager.gameTimer - prevTime < Constants.COLLISION_TIME_DELAY)
      return

    for (layer <- layers.indices.reverse) { // Parcourir les couches de la plus haute à la plus basse
      val collidedBox = layers(layer).find(_.checkMouseCollision(mouseX, mouseY))
      if (collidedBox.isDefined) {
        // Une collision a été trouvée dans ce layer, donc on arrête
        return
      }
    }
    prevTime = GameManager.gameTimer
  }

  /**
  * Handles mouse actions - Must be bound to mouse related events
  *
  * 1. mouse pressed
  *
  * 2. mouse released
  *
  * @param isPressed true if mouse is pressed - false if mouse is released
  */
  private def handleMouseAction(mouseButton: Int, isPressed: Boolean): Unit = {
    if(isPressed && !isMouseDown) {
      isMouseDown = true
      for (layer <- layers.indices.reverse) {
        val pressedBox = layers(layer).find(_.mousePressed(mouseButton))
        if (pressedBox.isDefined) {
          // Une boîte a été pressée dans ce layer, donc on arrête
          return
        }
      }
    }
    else if(!isPressed && isMouseDown){
      isMouseDown = false
      for (layer <- layers.indices.reverse) {
        val releasedBox = layers(layer).find(_.mouseReleased(mouseButton))
        if (releasedBox.isDefined) {
          // Une boîte a été relâchée dans ce layer, donc on arrête
          return
        }
      }
    }
  }

}

