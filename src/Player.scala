class Player(
              _pos: (Int, Int) = Constants.PLAYER_DEFAULT_POS,
              _hp: Int = Constants.PLAYER_DEFAULT_HP,
              _armor: Int = Constants.PLAYER_DEFAULT_ARMOR,
              _baseImagePath: String = Constants.PLAYER_DEFAULT_IMAGE_PATH,
              _velocity: Int = Constants.PLAYER_DEFAULT_VELOCITY,
              _damage: Int = Constants.PLAYER_DEFAULT_DAMAGE,
              var coins: Int = Constants.PLAYER_DEFAULT_COINS
            ) extends Character(_pos, _hp, _armor, _baseImagePath, _velocity, _damage) {

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
  this.playAnimation("idle")

  override def startMoving(): Unit = {
    super.startMoving()
    this.playAnimation("walk")
  }

  override def stopMoving(): Unit = {
    super.stopMoving()
    this.playAnimation("idle")
  }

}
