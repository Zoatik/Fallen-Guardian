class Tower(
             _pos: (Int, Int) = Constants.TOWER_DEFAULT_POS,
             _hp: Int = Constants.TOWER_DEFAULT_HP,
             _armor: Int = Constants.TOWER_DEFAULT_ARMOR,
             _lvl: Int = 1,
             _baseImagePath: String = Constants.TOWER_DEFAULT_IMAGE_PATH,
             _price: Int = 3,
             _blockPath: Boolean = true,
             _isTargetable: Boolean = true,
             var damage: Int = Constants.TOWER_DEFAULT_DAMAGE,
             var range: Int = Constants.TOWER_DEFAULT_RANGE,
             var attackSpeed: Double = Constants.TOWER_DEFAULT_ATTACK_SPEED,
             var target: Option[Enemy] = None
           ) extends Building(_pos, _hp, _armor, _lvl, _baseImagePath, _price, _blockPath, _isTargetable) {


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
      //val currentDist: Double = this.absDistanceTo(enemy)
      //je ne savais pas comment accéder à cette distance dans la partie towerTryToAttack
      //j'ai donc créer une variable globale currentDist.
      currentDist = this.absDistanceTo(enemy)
      if (currentDist < minDist) {
        minDist = currentDist
        closestTarget = Some(enemy)
      }
    })
    closestTarget
  }

  var currentDist: Double = Double.PositiveInfinity
  def towerTryToAttack(): Unit = {
    target = findTarget()
    //have to make an alternative to the cooldown function like there was in the character class
    //and use the attack speed value instead.
    if (target.isDefined && (currentDist < range)) {
      towerAttack()
    }
    if(!target.isDefined) {
      //println("Tower is currently looking for a target.")
    }
  }

  protected def towerAttack(): Unit = {
    if(target.isDefined){
      if (target.get.takeDamage(damage, this)) {
        EntitiesManager.destroyEntity(target.get)
        println(s"-------------------Tower has dealt damage.")
      }
    }

  }

  //tout crash lorsque j'essaie d'activer cette fonction pour l'animation

  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_TOWER_IDLE,
    duration = 1000,
    loop = true,
    active = true
  ))





  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)
  this.playAnimation("idle")
}
