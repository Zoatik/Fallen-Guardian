class Player(
              _pos: (Int, Int) = Constants.PLAYER_DEFAULT_POS,
              _hp: Int = Constants.PLAYER_DEFAULT_HP,
              _armor: Int = Constants.PLAYER_DEFAULT_ARMOR,
              _lvl: Int = 1,
              _baseImagePath: String = Constants.PLAYER_DEFAULT_IMAGE_PATH,
              _velocity: Double = Constants.PLAYER_DEFAULT_VELOCITY,
              _damage: Int = Constants.PLAYER_DEFAULT_DAMAGE,
              var coins: Int = Constants.PLAYER_DEFAULT_COINS
            ) extends Character(_pos, _hp, _armor, _lvl, _baseImagePath, _velocity, _damage) {


  override val attackCooldown: Int = 400

  def this(pos: (Int, Int), lvl: Int) =
    this(
      _pos = pos,
      _hp = Constants.PLAYER_DEFAULT_HP * lvl,
      _armor = Constants.PLAYER_DEFAULT_ARMOR * lvl,
      _lvl = lvl,
      _baseImagePath = Constants.PLAYER_DEFAULT_IMAGE_PATH,
      _velocity = Constants.PLAYER_DEFAULT_VELOCITY,
      _damage = Constants.PLAYER_DEFAULT_DAMAGE * lvl
    )

  override def levelUp(): Unit = {
    super.levelUp()
    this.hp = Constants.PLAYER_DEFAULT_HP * lvl
    this.damage = Constants.PLAYER_DEFAULT_DAMAGE * lvl
    this.armor = Constants.PLAYER_DEFAULT_ARMOR * lvl
  }


  def setTarget(entity: Enemy): Unit = {
    this.target = Some(entity)
    updateTargetPos()
  }

  override def updateTarget(): Unit = {}

  override protected def attack(): Unit = {
    super.attack()
    if(!isStunned && target.isDefined)
      if(target.get.takeDamage(damage, this)){
        coins += target.get.asInstanceOf[Enemy].getLvl()
        hasReachedTarget = false
        EntitiesManager.destroyEntity(target.get)
        println(s"COINS : $coins")
      }
  }


  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_SOLDIER_IDLE,
    duration = 1000,
    loop = true,
    active = true
  ))

  this.addAnimation("hurt", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_SOLDIER_HURT,
    duration = 300,
    loop = false,
    active = false
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
  this.playAnimation("idle")

  this.animations("hurt").onAnimationEnded(() => playAnimation("idle"))
  this.animations("attack1").onAnimationEnded(() => playAnimation("idle"))
  this.animations("attack1").onAnimationStarted(() => isAttacking = true)
  this.animations("attack1").onAnimationEnded(() => isAttacking = false)


}
