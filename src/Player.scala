class Player(
              _pos: (Int, Int) = Constants.PLAYER_DEFAULT_POS,
              _hp: Int = Constants.PLAYER_DEFAULT_HP,
              _armor: Int = Constants.PLAYER_DEFAULT_ARMOR,
              _baseImagePath: String = Constants.PLAYER_DEFAULT_IMAGE_PATH,
              _velocity: Int = Constants.PLAYER_DEFAULT_VELOCITY,
              _damage: Int = Constants.PLAYER_DEFAULT_DAMAGE,
              var coins: Int = Constants.PLAYER_DEFAULT_COINS
            ) extends Character(_pos, _hp, _armor, _baseImagePath, _velocity, _damage) {

  def setTarget(entity: Entity): Unit = {
    this.target = Some(entity)
  }

  def updateTargetPos(): Unit = {
    this.calculatePath(target.getOrElse(return).getPosition()._1, target.getOrElse(return).getPosition()._2)
  }

  override def updateTarget(): Unit = {}


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
