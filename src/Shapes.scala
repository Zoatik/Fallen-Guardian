import scala.collection.mutable

// Représentation d'une boîte en 2D
case class Box(x: Int, y: Int, width: Int, height: Int) {
  def intersects(other: Box): Boolean = {
    !(x + width <= other.x || other.x + other.width <= x ||
      y + height <= other.y || other.y + other.height <= y)
  }
}

// Classe 2DBoxCollision
class Box2DCollision(val id: String, initialBox: Box) {
  private var box: Box = initialBox
  private val listeners: mutable.ListBuffer[Box2DCollision => Unit] = mutable.ListBuffer()

  def setPosition(newX: Int, newY: Int): Unit = {
    box = box.copy(x = newX, y = newY)
    checkCollisions()
  }

  def setSize(newWidth: Int, newHeight: Int): Unit = {
    box = box.copy(width = newWidth, height = newHeight)
    checkCollisions()
  }

  def onCollision(listener: Box2DCollision => Unit): Unit = {
    listeners += listener
  }

  private def checkCollisions(): Unit = {
    Box2DCollisionManager.checkCollision(this).foreach { collidingBox =>
      listeners.foreach(_(collidingBox))
    }
  }

  def getBox: Box = box
}

// Gestionnaire de collisions global
object Box2DCollisionManager {
  private val boxes: mutable.ListBuffer[Box2DCollision] = mutable.ListBuffer()

  def register(box: Box2DCollision): Unit = boxes += box

  def unregister(box: Box2DCollision): Unit = boxes -= box

  def checkCollision(target: Box2DCollision): Seq[Box2DCollision] = {
    boxes.filter(other => other != target && other.getBox.intersects(target.getBox)).toSeq
  }
}