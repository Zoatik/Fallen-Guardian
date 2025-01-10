class Player(
              _pos: (Int, Int) = Constants.PLAYER_DEFAULT_POS,
              _hp: Int = Constants.PLAYER_DEFAULT_HP,
              _armor: Int = Constants.PLAYER_DEFAULT_ARMOR,
              _baseImagePath: String = Constants.PLAYER_DEFAULT_IMAGE_PATH,
              _velocity: Double = Constants.PLAYER_DEFAULT_VELOCITY,
              _damage: Int = Constants.PLAYER_DEFAULT_DAMAGE,
              var coins: Int = Constants.PLAYER_DEFAULT_COINS
            ) extends Character(_pos, _hp, _armor, _baseImagePath, _velocity, _damage) {


  def setTarget(entity: Enemy): Unit = {
    this.target = Some(entity)
    updateTargetPos()
  }



  override def updateTarget(): Unit = {}

  override protected def attack(): Unit = {
    super.attack()
    if(!isStuned && target.isDefined)
      if(target.get.takeDamage(damage, this)){
        coins += target.get.getHp()
        hasReachedTarget = false
        EntitiesManager.destroyEntity(target.get)
      }
  }


  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_SOLDIER_IDLE,
    duration = 1000,
    loop = true,
    active = true
  ))

  this.addAnimation("walk", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_SOLDIER_WALK,
    duration = 1000,
    loop = true,
    active = false
  ))

  this.addAnimation("attack1", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_SOLDIER_ATTACK_1,
    duration = 100,
    loop = false,
    active = false
  ))

  Layers.addSprite(Constants.LAYER_PLAYER, this.sprite)
  this.animations("attack1").onAnimationEnded(() => playAnimation("idle"))
  this.playAnimation("idle")

  this.animations("attack1").onAnimationStarted(() => isAttacking = true)
  this.animations("attack1").onAnimationEnded(() => isAttacking = false)

}
