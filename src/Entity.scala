import Constants._

import scala.collection.mutable
/**
 * Base class for entities
 * @param pos             relative position to grid
 * @param hp              health
 * @param armor           armor
 * @param baseImagePath   base Image path
 */
abstract class Entity(
              protected var pos: (Int, Int),
              protected var maxHp: Int,
              protected var armor: Int,
              protected var lvl: Int,
              protected val baseImageBitmap: BetterGraphicsBitmap,
              protected val spriteAnchor: Int = ANCHOR_BOTTOM_MIDDLE
) {

  protected var hp: Int = maxHp
  val sprite: Sprite = new Sprite(baseImageBitmap, _anchor = spriteAnchor)
  protected var absPos: (Int, Int) = (pos._1 * CELL_SIZE, pos._2 * CELL_SIZE)

  private val spritePos = spriteAnchor match {
    case ANCHOR_TOP_LEFT => (pos._1 * CELL_SIZE, pos._2 * CELL_SIZE)
    case ANCHOR_MIDDLE => (pos._1 * CELL_SIZE + CELL_SIZE/2, pos._2 * CELL_SIZE + CELL_SIZE/2)
    case ANCHOR_BOTTOM_MIDDLE => (pos._1 * CELL_SIZE + CELL_SIZE/2, pos._2 * CELL_SIZE + CELL_SIZE)
  }

  sprite.setPosition(spritePos)


  val collisionBox2D: CollisionBox2D

  setCollisionListeners()

  val animations: mutable.Map[String, Animation] = mutable.Map()

  def levelUp(): Unit = {
    lvl += 1
  }

  def destroy(): Unit = {
    Renderer.destroy(sprite)
    CollisionBox2DManager.destroy(collisionBox2D)
    AnimationsManager.destroy(animations.values)
  }

  def isAlive: Boolean = hp > 0

  def takeDamage(amount: Int, source: Entity): Boolean = {
    val effectiveAmount = Math.max(amount - armor, 0)
    hp -= effectiveAmount
    if (hp <= 0)
      hp = 0

    hp == 0
  }

  def updateTarget(): Unit = {}

  def findTarget(): Option[Entity] = None

  def addAnimation(id: String, newAnimation: Animation): Unit = animations.update(id, newAnimation)

  def playAnimation(id: String): Unit = {
    stopAllAnimations()
    val anim = animations.getOrElse(id,   {println(s"animation $id not found"); return})
    anim.activate()
    anim.play()
  }

  def stopAnimation(id: String): Unit = {
    animations.getOrElse(id, return).stop()
    animations(id).deactivate()
  }

  def stopAllAnimations(): Unit = {
    animations.keys.foreach(id => stopAnimation(id))
  }

  def isAnimationPlaying(): Boolean = {
    for(anim <- animations.values){
      if(anim.playing)
        return true
    }
    false
  }


  def absDistanceTo(otherPos: (Int, Int)): Double = {
    math.sqrt(math.pow(otherPos._1 - this.absPos._1, 2) + math.pow(otherPos._2 - this.absPos._2, 2))
  }

  def absDistanceTo(other: Entity): Double = {
    val otherPos: (Int, Int) = other.absPos
    absDistanceTo(otherPos)
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
    sprite.setTopLeftPosition(absolutePosX, absolutePosY)
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
    sprite.setPosition(newAbsPos._1 + sprite.bm.getWidth/2, newAbsPos._2 + sprite.bm.getHeight)
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

  def setCollisionListeners(): Unit = {
    if(collisionBox2D != null) {
      collisionBox2D.onMouseReleased(mouseButton => mouseReleased(mouseButton))
      collisionBox2D.onMouseEnter(() => mouseEntered())
      collisionBox2D.onMouseLeave(() => mouseLeft())
    }
  }

  def mouseReleased(mouseButton: Int): Unit = {
    GameManager.handleEntityMouseAction(mouseButton, pressed = false, this)
  }

  private def mouseEntered(): Unit = {
    sprite.brighten(2)
    GameManager.handleOnEntityEntered(this)
  }

  private def mouseLeft(): Unit = {
    sprite.restoreImage(this.baseImageBitmap)
    GameManager.handleOnEntityLeft(this)
  }

  def getHp(): Int = hp

  def getMaxHp(): Int = maxHp

  def getLvl(): Int = lvl
}

