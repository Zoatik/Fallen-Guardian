import hevs.graphics.utils.GraphicsBitmap

class Building(_pos: (Int, Int),
               _hp: Int,
               _armor: Int,
               _lvl: Int,
               _baseImageBitmap: GraphicsBitmap,
               var price: Int,
               var blockPath: Boolean,
               var isTargetable: Boolean
              ) extends Entity(_pos, _hp, _armor, _lvl, _baseImageBitmap){

  override def levelUp(): Unit = {
    price *= 2
  }

}
