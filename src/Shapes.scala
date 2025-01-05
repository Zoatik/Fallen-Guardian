import java.awt.event.MouseEvent
import scala.collection.mutable

// Box 2D representation
case class Box(x: Int, y: Int, width: Int, height: Int) {

  def containsPoint(px: Int, py: Int): Boolean = {
    px >= x && px < x + width && py >= y && py < y + height
  }
}

// 2DBoxCollision Class
class CollisionBox2D (val id: String, initialBox: Box) {
  private var box: Box = initialBox
  private val mouseEnterListeners: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()
  private val mouseLeaveListeners: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()
  private val mousePressedListeners: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()
  private val mouseReleasedListeners: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()

  var isMouseOver: Boolean = false

  def setPosition(newX: Int, newY: Int): Unit = {
    box = box.copy(x = newX, y = newY)
  }

  def setSize(newWidth: Int, newHeight: Int): Unit = {
    box = box.copy(width = newWidth, height = newHeight)
  }


  def onMouseEnter(listener: () => Unit): Unit = {
    mouseEnterListeners += listener
  }

  def onMouseLeave(listener: () => Unit): Unit = {
    mouseLeaveListeners += listener
  }

  def onMousePressed(listener: () => Unit): Unit = {
    mousePressedListeners += listener
  }

  def onMouseReleased(listener: () => Unit): Unit = {
    mouseReleasedListeners += listener
  }

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

  private def mouseAction(isPressed: Boolean): Unit = {
    if (isPressed)
      mousePressed()
    else
      mouseReleased()
  }

  def mousePressed(): Boolean = {
    if(!isMouseOver)
      return false
    mousePressedListeners.foreach(_())
    true
  }

  def mouseReleased(): Boolean = {
    if(!isMouseOver)
      return false
    mouseReleasedListeners.foreach(_())
    true
  }

  def getBox: Box = box
}
  // Global collision Manager
  object CollisionBox2DManager {
    private val layers: Array[mutable.ListBuffer[CollisionBox2D]] = Array.fill(3)(mutable.ListBuffer[CollisionBox2D]())
    private var boxesCounter: Int = 0
    private var prevTime: Long = 0
    private var isMouseDown: Boolean = false

    InputManager.bindMouseMotion((x,y) => checkMouseCollisions(x,y))
    InputManager.bindMouseButton(MouseEvent.BUTTON1, isPressed => handleMouseAction(isPressed))

    // Ajouter une nouvelle boîte à un layer spécifique
    def newCollisionBox2D(initialBox: Box, layer: Int = 0): CollisionBox2D = {
      require(layer >= 0 && layer < layers.length, s"Invalid layer: $layer")
      val newBox = new CollisionBox2D(s"Box$boxesCounter", initialBox)
      register(newBox, layer)
      newBox
    }

    // Enregistrer une boîte dans un layer spécifique
    def register(box: CollisionBox2D, layer: Int): Unit = {
      require(layer >= 0 && layer < layers.length, s"Invalid layer: $layer")
      layers(layer) += box
      boxesCounter += 1
    }

    // Désenregistrer une boîte d'un layer spécifique
    def unregister(box: CollisionBox2D, layer: Int): Unit = {
      require(layer >= 0 && layer < layers.length, s"Invalid layer: $layer")
      layers(layer) -= box
    }

    // Vérifier les collisions avec la souris sur le layer le plus haut
    def checkMouseCollisions(mouseX: Int, mouseY: Int): Unit = {
      if(System.currentTimeMillis() - prevTime < Constants.COLLISION_TIME_DELAY)
        return

      for (layer <- layers.indices.reverse) { // Parcourir les couches de la plus haute à la plus basse
        val collidedBox = layers(layer).find(_.checkMouseCollision(mouseX, mouseY))
        if (collidedBox.isDefined) {
          // Une collision a été trouvée dans ce layer, donc on arrête
          return
        }
      }
      prevTime = System.currentTimeMillis()
    }

    // Gérer les événements de souris pressée pour tous les layers
    private def handleMouseAction(isPressed: Boolean): Unit = {
      if(isPressed && !isMouseDown) {
        isMouseDown = true
        for (layer <- layers.indices.reverse) {
          val pressedBox = layers(layer).find(_.mousePressed())
          if (pressedBox.isDefined) {
            // Une boîte a été pressée dans ce layer, donc on arrête
            return
          }
        }
      }
      else if(!isPressed && isMouseDown){
        isMouseDown = false
        for (layer <- layers.indices.reverse) {
          val releasedBox = layers(layer).find(_.mouseReleased())
          if (releasedBox.isDefined) {
            // Une boîte a été relâchée dans ce layer, donc on arrête
            return
          }
        }
      }
    }

    // Gérer les événements de souris relâchée pour tous les layers
    def handleMouseReleased(): Unit = {
      for (layer <- layers.indices.reverse) {
        val releasedBox = layers(layer).find(_.mouseReleased())
        if (releasedBox.isDefined) {
          // Une boîte a été relâchée dans ce layer, donc on arrête
          return
        }
      }
    }
  }

