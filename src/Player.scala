import Constants._

class Player(
              _pos: (Int, Int) = Constants.PLAYER_DEFAULT_POS,
              _maxHp: Int = Constants.PLAYER_DEFAULT_HP,
              _armor: Int = Constants.PLAYER_DEFAULT_ARMOR,
              _lvl: Int = 1,
              _baseImageBitmap: BetterGraphicsBitmap = Constants.PLAYER_DEFAULT_IMAGE_BITMAP,
              _velocity: Double = Constants.PLAYER_DEFAULT_VELOCITY,
              _damage: Int = Constants.PLAYER_DEFAULT_DAMAGE,
              var coins: Int = Constants.PLAYER_DEFAULT_COINS
            ) extends Character(_pos, _maxHp, _armor, _lvl, _baseImageBitmap, _velocity, _damage) {


  override val collisionBox2D: CollisionBox2D = CollisionBox2DManager.newCollisionBox2D(Box(
    x = sprite.getTopLeftPos()._1,
    y = sprite.getTopLeftPos()._2,
    width = sprite.bm.getWidth,
    height = sprite.bm.getHeight
  ), layer = COLLISION_LAYER_PLAYER)

  setCollisionListeners()

  override val attackCooldown: Int = 400
  var xp: Int = 0

  // player game mode infos
  var isBuilding = false
  var buildSelected: Int = Constants.BUILD_TOWER


  def this(pos: (Int, Int), lvl: Int) =
    this(
      _pos = pos,
      _maxHp = Constants.PLAYER_DEFAULT_HP * lvl,
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
    this.maxHp = Constants.PLAYER_DEFAULT_HP * lvl
    this.hp = maxHp
    this.damage = Constants.PLAYER_DEFAULT_DAMAGE * lvl
    this.armor = Constants.PLAYER_DEFAULT_ARMOR * lvl
  }

  override def destroy(): Unit = {
    super.destroy()
    Renderer.destroy(playerHealthBarSprite)
  }

  def gainXP(amount: Int): Unit = {
    xp += amount
    if(xp > 1000){
      xp = 0
      levelUp()
    }
  }

  override def updateTarget(newTarget: Option[Entity]): Unit = {
    if (newTarget.isEmpty || !newTarget.get.isInstanceOf[Enemy])
      return

    target = newTarget
    this.updateTargetPos()
  }

  def updateTargetPos(): Unit = {
    if(target.isEmpty)
      return
    this.calculatePath(target.get.getPosition()._1, target.get.getPosition()._2)
  }



  override protected def attack(): Unit = {
    //trying death vs. normal attack slashes.. look in Enemy.scala
    //Constants.PLAYER_ATTACK_AUDIO.play()
    super.attack()
    if(target.isDefined)
      if(target.get.takeDamage(damage, this)){
        coins += target.get.asInstanceOf[Enemy].getLvl
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

  var playerHealthBarSprite: Sprite = new Sprite(Constants.PLAYER_HP_BAR_1, this.getAbsPosition)
  Layers.addSprite(Constants.LAYER_UI_MOBILE, playerHealthBarSprite)

}
