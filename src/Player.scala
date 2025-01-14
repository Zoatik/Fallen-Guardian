
class Player(
              _pos: (Int, Int) = Constants.PLAYER_DEFAULT_POS,
              _hp: Int = Constants.PLAYER_DEFAULT_HP,
              _armor: Int = Constants.PLAYER_DEFAULT_ARMOR,
              _lvl: Int = 1,
              _baseImageBitmap: BetterGraphicsBitmap = Constants.PLAYER_DEFAULT_IMAGE_BITMAP,
              _velocity: Double = Constants.PLAYER_DEFAULT_VELOCITY,
              _damage: Int = Constants.PLAYER_DEFAULT_DAMAGE,
              var coins: Int = Constants.PLAYER_DEFAULT_COINS
            ) extends Character(_pos, _hp, _armor, _lvl, _baseImageBitmap, _velocity, _damage) {


  override val attackCooldown: Int = 400
  var xp: Int = 0

  // player game mode infos
  var isBuilding = false
  var buildSelected: Int = Constants.BUILD_TOWER


  def this(pos: (Int, Int), lvl: Int) =
    this(
      _pos = pos,
      _hp = Constants.PLAYER_DEFAULT_HP * lvl,
      _armor = Constants.PLAYER_DEFAULT_ARMOR * lvl,
      _lvl = lvl,
      _baseImageBitmap = Constants.PLAYER_DEFAULT_IMAGE_BITMAP,
      _velocity = Constants.PLAYER_DEFAULT_VELOCITY,
      _damage = Constants.PLAYER_DEFAULT_DAMAGE * lvl
    )

  override def takeDamage(amount: Int, source: Entity): Boolean = {
    Constants.PLAYER_HIT_AUDIO.play()
    super.takeDamage(amount, source)
  }

  override def levelUp(): Unit = {
    super.levelUp()
    this.hp = Constants.PLAYER_DEFAULT_HP * lvl
    this.damage = Constants.PLAYER_DEFAULT_DAMAGE * lvl
    this.armor = Constants.PLAYER_DEFAULT_ARMOR * lvl
  }

  def gainXP(amount: Int): Unit = {
    xp += amount
    if(xp > 1000){
      xp = 0
      levelUp()
    }
  }


  def setTarget(entity: Enemy): Unit = {
    this.target = Some(entity)
    updateTargetPos()
  }

  override def updateTarget(): Unit = {}

  override protected def attack(): Unit = {
    //trying death vs. normal attack slashes.. look in Enemy.scala
    //Constants.PLAYER_ATTACK_AUDIO.play()
    super.attack()
    if(!isStunned && target.isDefined)
      if(target.get.takeDamage(damage, this)){
        coins += target.get.asInstanceOf[Enemy].getLvl()
        gainXP(100)
        EntitiesManager.destroyEntity(target.get)
        println(s"COINS : $coins")
      }
  }

  def playerMoveAudio(): Audio = {
    var ad: Audio = new Audio(s"/res/Audio/PlayerAudio/WalkAudio/Walk_-${(math.random() * (14-1) + 2).toInt}.wav")
    ad
  }

  def build(cell: Cell): Boolean = {
    val price = buildSelected match {
      case Constants.BUILD_TOWER => Constants.BUILD_TOWER_PRICE
      case Constants.BUILD_BARRICADE => Constants.BUILD_BARRICADE_PRICE
    }

    if(coins >= price) {
      coins -= price
      Grid.build(cell, buildSelected, lvl)
      return true
    }
    false
  }

  def canAffordBuild(): Boolean = {
    val price = buildSelected match {
      case Constants.BUILD_TOWER => Constants.BUILD_TOWER_PRICE
      case Constants.BUILD_BARRICADE => Constants.BUILD_BARRICADE_PRICE
    }

    coins >= price
  }

  def upgrade(building: Building): Boolean = {
    var price: Int = 0
    price = building.price
    if(coins >= price){
      coins -= price
      building.levelUp()
      return true
    }
    false
  }

  def canAffordUpgrade(building: Building): Boolean = {
    coins >= building.price
  }

  def sell(building: Building): Unit = {
    building match {
      case tower: Tower => coins += tower.price / 2
    }
    Grid.removeBuilding(building)
    println("tower sold, coins : " + coins)
  }


  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_SOLDIER_IDLE,
    duration = 1000,
    loop = true,
    active = true
  ))

  this.addAnimation("hurt", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_SOLDIER_HURT,
    duration = 300,
    loop = false,
    active = false
  ))

  this.addAnimation("walk", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_SOLDIER_WALK,
    duration = 1000,
    loop = true,
    active = false
  ))

  this.addAnimation("attack1", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_SOLDIER_ATTACK_1,
    duration = 100,
    loop = false,
    active = false
  ))

  Layers.addSprite(Constants.LAYER_PLAYER, this.sprite)
  this.playAnimation("idle")

  this.animations("hurt").onAnimationEnded(() => playAnimation("idle"))
  this.animations("attack1").onAnimationEnded(() => {
    if(!this.isAnimationPlaying())
      playAnimation("idle")
  })
  this.animations("attack1").onAnimationStarted(() => isAttacking = true)
  this.animations("attack1").onAnimationEnded(() => isAttacking = false)


}
