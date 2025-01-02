import scala.collection.mutable

// Représentation d'une boîte en 2D
case class Box(x: Int, y: Int, width: Int, height: Int) {

  def containsPoint(px: Int, py: Int): Boolean = {
    px >= x && px <= x + width && py >= y && py <= y + height
  }
}

// Classe 2DBoxCollision
class CollisionBox2D (val id: String, initialBox: Box) {
  private var box: Box = initialBox
  private val mouseEnterListeners: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()
  private val mouseLeaveListeners: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()

  private var isMouseOver: Boolean = false

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

  def getBox: Box = box
}
  // Gestionnaire de collisions global
  object CollisionBox2DManager {
    private val boxes: mutable.ListBuffer[CollisionBox2D] = mutable.ListBuffer()
    private var boxesCounter: Int = 0
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
  }

