class Enemy(
             _pos: (Int, Int),
             _hp: Int,
             _baseImagePath: String,
             _velocity: Double,
             _damage: Int,
             _armor: Int
           ) extends Character(_pos, _hp, _armor, _baseImagePath, _velocity, _damage) {

  override def updateTarget(): Unit = {}

  override protected def attack(): Unit = {
    super.attack()
    if(!isStuned && target.isDefined){
      println("ENEMY ATTACKED")
      if(target.get.takeDamage(damage, this)){
        hasReachedTarget = false
        EntitiesManager.destroyEntity(target.get)
      }
    }
  }

  override def takeDamage(amount: Int, source: Entity): Boolean = {
    isStuned = true
    lastTimeStuned = System.currentTimeMillis()
    val isDead = super.takeDamage(amount, source)
    this.target = Some(source)
    this.updateTargetPos()
    isDead
  }

  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_ORC_IDLE,
    duration = 1000,
    loop = true,
    active = true
  ))

  this.addAnimation("hurt", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_ORC_HURT,
    duration = 300,
    loop = false,
    active = false
  ))

  this.addAnimation("attack1", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_ORC_ATTACK_1,
    duration = 300,
    loop = false,
    active = false
  ))

  this.addAnimation("walk", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_ORC_WALK,
    duration = 1000,
    loop = true,
    active = false
  ))

  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)
  this.playAnimation("idle")

  this.animations("attack1").onAnimationEnded(() => playAnimation("idle"))
  this.animations("attack1").onAnimationStarted(() => isAttacking = true)
  this.animations("attack1").onAnimationEnded(() => isAttacking = false)


}
