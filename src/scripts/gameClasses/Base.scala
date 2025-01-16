package scripts.gameClasses

import scripts.data.Constants.COLLISION_LAYER_BUILDING
import scripts.data.{AnimationsResources, Constants}
import scripts.tools._

class Base(
            _pos: (Int, Int),
            _maxHp: Int,
            _armor: Int,
            _lvl: Int = 1,
            _baseImageBitmap: BetterGraphicsBitmap,
            _spriteAnchor: Int
           ) extends Entity(_pos, _maxHp, _armor, _lvl, _baseImageBitmap, _spriteAnchor) {


  for(i <- 0 until 3; j <- 0 until 3){
    Grid.getCell(this.pos._1 + i, this.pos._2 + j).get.state = CellStates.BLOCK_PATH
  }

  def this() = this(
    _pos = Constants.BASE_DEFAULT_POS,
    _maxHp = Constants.BASE_DEFAULT_HP,
    _armor = Constants.BASE_DEFAULT_ARMOR,
    _lvl = 1,
    _baseImageBitmap = Constants.BASE_DEFAULT_IMAGE_BITMAP,
    _spriteAnchor = Constants.ANCHOR_TOP_LEFT
  )

  override val collisionBox2D: CollisionBox2D = CollisionBox2DManager.newCollisionBox2D(Box(
    x = sprite.getTopLeftPos()._1,
    y = sprite.getTopLeftPos()._2,
    width = sprite.bm.getWidth,
    height = sprite.bm.getHeight
  ), layer = COLLISION_LAYER_BUILDING)

  override def takeDamage(amount: Int, source: Entity): Boolean = {
    val isDead: Boolean = super.takeDamage(amount, source)
    playAnimation("hurt")
    isDead
  }

  this.addAnimation("hurt", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_BASE_HURT,
    duration = 1000,
    loop = false,
    active = false
  ))

  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)
}