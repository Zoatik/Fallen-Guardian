class Building(_pos: (Int, Int),
               _hp: Int,
               _armor: Int,
               _baseImagePath: String,
               var price: Int,
               var blockPath: Boolean,
               var isTargetable: Boolean
              ) extends Entity(_pos, _hp, _armor, _baseImagePath){

}
