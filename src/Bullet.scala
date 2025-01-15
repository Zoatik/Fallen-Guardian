import Constants._

import scala.collection.mutable

class Bullet(
              _pos: (Int, Int),
              _maxHp: Int,
              _baseImageBitmap: BetterGraphicsBitmap,
              _armor: Int,
              _lvl: Int,
              var sourceTower: Tower,
              var target: Option[Entity],
              var velocity: Int
           ) extends Entity(_pos, _maxHp, _armor, _lvl,  _baseImageBitmap) {

  protected val pathQueue: mutable.Queue[(Int, Int)] = mutable.Queue()
  private var direction: (Int, Int) = (0,0)

  override val collisionBox2D: CollisionBox2D = CollisionBox2DManager.newCollisionBox2D(Box(
    x = sprite.getTopLeftPos()._1,
    y = sprite.getTopLeftPos()._2,
    width = sprite.bm.getWidth,
    height = sprite.bm.getHeight
  ), layer = COLLISION_LAYER_BUILDING)

  setCollisionListeners()

  def this(posOffset: (Int, Int), newSourceTower: Tower) = this(
    _pos = (newSourceTower.getPosition()._1 + posOffset._1, newSourceTower.getPosition()._2 + posOffset._2),
    _maxHp = 0,
    _baseImageBitmap = Constants.BULLET_DEFAULT_IMAGE_BITMAP,
    _armor = 0,
    _lvl = 0,
    sourceTower = newSourceTower,
    target = newSourceTower.target,
    velocity = Constants.BULLET_VELOCITY
  )

  private def move(deltaX: Int, deltaY: Int): Unit = {
    val newX = this.getAbsPosition._1 + deltaX
    val newY = this.getAbsPosition._2 + deltaY
    this.setAbsPosition((newX, newY))
  }

  def moveToTarget(): Unit = {
    if(hasReachedTarget()){
      hit()
    }
    else if(target.isEmpty){
      EntitiesManager.destroyEntity(this)
    }
    else {
      move(direction._1, direction._2)
    }

  }

  private def hasReachedTarget(): Boolean = {
    target.isDefined && collisionBox2D.collidesWith(target.get.collisionBox2D)
  }

  def refreshPath(): Unit = {
    pathQueue.removeAll()
    if(target.isDefined) {
      val targetPos: (Int, Int) = target.get.getAbsPosition
      val pathVect: (Int, Int) = (targetPos._1 - this.getAbsPosition._1, targetPos._2 - this.getAbsPosition._2)
      val dist: Double = this.absDistanceTo(targetPos)
      val pathVectNormalized: (Int, Int) = ((pathVect._1 / dist * velocity * (1 + Renderer.deltaT) / 10).toInt,
                                            (pathVect._2 / dist * velocity * (1 + Renderer.deltaT) / 10).toInt)
      direction = pathVectNormalized
    }

  }

  def hit(): Unit = {
    if (target.get.takeDamage(sourceTower.damage, sourceTower)) {
        EntitiesManager.player.get.coins += target.get.getLvl
        EntitiesManager.destroyEntity(target.get)
      }
    EntitiesManager.destroyEntity(this)
  }

  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_TOWER_BULLET,
    duration = 600,
    loop = true,
    active = true
  ))


  Layers.addSprite(Constants.LAYER_PLAYER, this.sprite)
  this.playAnimation("idle")


}