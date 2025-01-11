class Tower(
             _pos: (Int, Int) = Constants.TOWER_DEFAULT_POS,
             _hp: Int = Constants.TOWER_DEFAULT_HP,
             _armor: Int = Constants.TOWER_DEFAULT_ARMOR,
             _baseImagePath: String = Constants.TOWER_DEFAULT_IMAGE_PATH,
             _price: Int = 3,
             _blockPath: Boolean = true,
             _isTargetable: Boolean = true,
             var damage: Int = Constants.TOWER_DEFAULT_DAMAGE,
             var range: Int = Constants.TOWER_DEFAULT_RANGE,
             var attackSpeed: Double = Constants.TOWER_DEFAULT_ATTACK_SPEED,
             var target: Option[Enemy] = None
           ) extends Building(_pos, _hp, _armor, _baseImagePath, _price, _blockPath, _isTargetable) {


  override def updateTarget(): Unit = {
    if (target.isDefined) {
      return
    }
    this.target = findTarget()
  }

  override def findTarget(): Option[Enemy] = {
    var minDist: Double = Double.PositiveInfinity
    var closestTarget: Option[Enemy] = None
    EntitiesManager.enemies.foreach(enemy => {
      val currentDist: Double = this.absDistanceTo(enemy)
      if (currentDist < minDist) {
        minDist = currentDist
        closestTarget = Some(enemy)
      }
    })
    closestTarget
  }


  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)
}
