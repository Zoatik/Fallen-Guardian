import Constants._

import scala.collection.mutable
/**
 * Base class for entities
 * @param pos             relative position to grid
 * @param hp              health
 * @param armor           armor
 * @param baseImagePath   base Image path
 */
class Entity(
              protected var pos: (Int, Int),
              protected var hp: Int,
              protected var armor: Int,
              protected val baseImagePath: String
) {
  val sprite: Sprite = new Sprite(baseImagePath)
  private var absPos: (Int, Int) = (pos._1 * sprite.bm.getWidth, pos._2 * sprite.bm.getHeight)
  sprite.setPosition(absPos)

  val collisionBox2D: CollisionBox2D = CollisionBox2DManager.newCollisionBox2D(Box(
    x = pos._1 * sprite.bm.getWidth,
    y = pos._2 * sprite.bm.getHeight,
    width = sprite.bm.getWidth,
    height = sprite.bm.getHeight
  ),
    layer = LAYER_ENTITIES
  )

  val animations: mutable.Map[String, Animation] = mutable.Map()

  def isAlive: Boolean = hp > 0

  def takeDamage(amount: Int): Unit = {
    val effectiveAmount = Math.max(amount - armor, 0)
    hp -= effectiveAmount
    if (hp < 0) hp = 0
  }

  def addAnimation(id: String, newAnimation: Animation): Unit = animations.update(id, newAnimation)

  def playAnimation(id: String): Unit = {
    stopAllAnimations()
    val anim = animations.getOrElse(id, {println(s"animation $id not found"); return})
    anim.activate()
    anim.play()
  }

  def stopAnimation(id: String): Unit = animations.getOrElse(id, return).stop()

  def stopAllAnimations(): Unit = animations.values.foreach(_.stop())


  def absDistanceTo(otherPos: (Int, Int)): Double = {
    math.sqrt(math.pow(otherPos._1 - this.absPos._1, 2) + math.pow(otherPos._2 - this.absPos._2, 2))
  }

  /**
   * Sets the retlative position to the grid
   * @param newPos new position relative to the grid
   */
  def setPosition(newPos: (Int, Int)): Unit = {
    pos = newPos
    val absolutePosX = Constants.CELL_SIZE * newPos._1
    val absolutePosY = Constants.CELL_SIZE * newPos._2
    absPos = (absolutePosX, absolutePosY)
    sprite.setPosition(absolutePosX, absolutePosY)
    collisionBox2D.setPosition(absolutePosX, absolutePosY)
  }

  /**
   * Sets the absolute position in the 2D space (pixels)
   * @param newAbsPos new absolute position
   */
  def setAbsPosition(newAbsPos: (Int, Int)): Unit ={
    absPos = newAbsPos
    val newPos = (newAbsPos._1 / Constants.CELL_SIZE, newAbsPos._2 / Constants.CELL_SIZE)
    pos = newPos
    sprite.setPosition(newAbsPos._1, newAbsPos._2)
    collisionBox2D.setPosition(newAbsPos._1, newAbsPos._2)
  }

  /**
   *
   * @return Relative position to the grid
   */
  def getPosition(): (Int, Int) = pos

  /**
   *
   * @return Absolute position in 2D space (pixels)
   */
  def getAbsPosition: (Int, Int) = absPos

}

