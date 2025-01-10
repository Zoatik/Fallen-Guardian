class Tower(
                _pos: (Int, Int),
                _hp: Int,
                _armor: Int,
                _baseImagePath: String,
                var damage: Int,
                var range: Double
              ) extends Entity(_pos, _hp, _armor, _baseImagePath) {
  protected var target: Option[Entity] = None

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
}
