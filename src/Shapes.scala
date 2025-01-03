import scala.collection.mutable

// Box 2D representation
case class Box(x: Int, y: Int, width: Int, height: Int) {

  def containsPoint(px: Int, py: Int): Boolean = {
    px >= x && px <= x + width && py >= y && py <= y + height
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
  var isMouseDown: Boolean = false

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

  def checkMouseCollision(mouseX: Int, mouseY: Int): Unit = {
    if (box.containsPoint(mouseX, mouseY) && !isMouseOver) {
      isMouseOver = true
      mouseEnterListeners.foreach(_())
    }
    else if(!box.containsPoint(mouseX, mouseY) && isMouseOver){
      isMouseOver = false
      mouseLeaveListeners.foreach(_())
    }
  }


  def mousePressed(): Unit = {
    if(isMouseOver && !isMouseDown) {
      mousePressedListeners.foreach(_())
      isMouseDown = true
    }
  }

  def mouseReleased(): Unit = {
    if(isMouseOver && isMouseDown) {
      mouseReleasedListeners.foreach(_())
      isMouseDown = false
    }
  }

  def getBox: Box = box
}
  // Global collision Manager
  object CollisionBox2DManager {
    private val boxes: mutable.ListBuffer[CollisionBox2D] = mutable.ListBuffer()
    private var boxesCounter: Int = 0
    var isMouseDown: Boolean = false

    def newCollisionBox2D(initialBox: Box): CollisionBox2D = {
      val newBox = new CollisionBox2D(s"Box$boxesCounter", initialBox)
      register(newBox)
      newBox
    }

    def register(box: CollisionBox2D): Unit = {
      boxes += box
      boxesCounter += 1
    }

    def unregister(box: CollisionBox2D): Unit = boxes -= box


    def checkMouseCollisions(mouseX: Int, mouseY: Int): Unit = {
      boxes.foreach(_.checkMouseCollision(mouseX, mouseY))
    }

    def handleMousePressed(): Unit = {
      boxes.foreach(_.mousePressed())
    }

    def handleMouseReleased(): Unit = {
      boxes.foreach(_.mouseReleased())
    }
  }

