import Constants._

class Building(_pos: (Int, Int),
               _hp: Int,
               _armor: Int,
               _lvl: Int,
               _baseImageBitmap: BetterGraphicsBitmap,
               var price: Int,
               var blockPath: Boolean,
               var isTargetable: Boolean
              ) extends Entity(_pos, _hp, _armor, _lvl, _baseImageBitmap){

  override val collisionBox2D: CollisionBox2D = CollisionBox2DManager.newCollisionBox2D(Box(
    x = sprite.getTopLeftPos()._1,
    y = sprite.getTopLeftPos()._2,
    width = sprite.bm.getWidth,
    height = sprite.bm.getHeight
  ), layer = COLLISION_LAYER_BUILDING)

  setCollisionListeners()

  override def levelUp(): Unit = {
    price *= 2
  }

}
