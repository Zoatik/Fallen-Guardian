class Bullet(
             _pos: (Int, Int),
             _hp: Int,
             _baseImagePath: String,
             _velocity: Double,
             _damage: Int,
             _armor: Int,
             _lvl: Int,
             var sourceTower: Tower
           ) extends Character(_pos, _hp, _armor, _lvl,  _baseImagePath, _velocity, _damage) {

  def this(pos: (Int, Int), newSourceTower: Tower) = this(
    _pos = pos,
    _hp = 0,
    _baseImagePath = Constants.BULLET_DEFAULT_IMAGE_PATH,
    _velocity = Constants.BULLET_VELOCITY,
    _damage = newSourceTower.damage,
    _armor = 0,
    _lvl = 0,
    sourceTower = newSourceTower
  )
  

  override def attack(): Unit = {
    if (target.get.takeDamage(damage, this)) {
        EntitiesManager.player.coins += target.get.getLvl()
        EntitiesManager.destroyEntity(target.get)
        println(s"-------------------bullet has dealt damage.")
      }
  }

  def updateAttack(): Unit = {
    if(target.isDefined){
      if(this.collisionBox2D.collidesWith(target.get.collisionBox2D)){
        attack()
        EntitiesManager.destroyEntity(this)
      }
    }
  }

  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)


}