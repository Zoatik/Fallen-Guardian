package scripts.gameClasses

import scripts.data.Constants._
import scripts.data.{AnimationsResources, Constants}
import scripts.managers.{EntitiesManager, GameManager}
import scripts.tools._
class Enemy(
             _pos: (Int, Int),
             _maxHp: Int,
             _baseImageBitmap: BetterGraphicsBitmap,
             _velocity: Double,
             _damage: Int,
             _armor: Int,
             _lvl: Int
           ) extends Character(_pos, _maxHp, _armor, _lvl,  _baseImageBitmap, _velocity, _damage) {

  def this(pos: (Int, Int), lvl: Int){
    this(
      _pos = pos,
      _maxHp = Constants.ENEMY_DEFAULT_HP * lvl,
      _baseImageBitmap = Constants.ENEMY_DEFAULT_IMAGE_BITMAP,
      _velocity = Constants.ENEMY_DEFAULT_VELOCITY,
      _damage = Constants.ENEMY_DEFAULT_DAMAGE * lvl,
      _armor = Constants.ENEMY_DEFAULT_ARMOR * lvl,
      _lvl = lvl
    )
  }

  override val collisionBox2D: CollisionBox2D = CollisionBox2DManager.newCollisionBox2D(Box(
    x = sprite.getTopLeftPos()._1,
    y = sprite.getTopLeftPos()._2,
    width = sprite.bm.getWidth,
    height = sprite.bm.getHeight
  ), layer = COLLISION_LAYER_ENEMIES)

  setCollisionListeners()

  override def levelUp(): Unit = {
    super.levelUp()
    this.maxHp = Constants.ENEMY_DEFAULT_HP * lvl
    this.hp = maxHp
    this.damage = Constants.ENEMY_DEFAULT_DAMAGE * lvl
    this.armor = Constants.ENEMY_DEFAULT_ARMOR * lvl
  }

  override def destroy(): Unit = {
    super.destroy()
    Renderer.destroy(enemyHealthBarSprite)
  }

  override val attackCooldown: Int = 1000

  override def updateTarget(newTarget: Option[Entity]): Unit = {
    if(target.isDefined && target.get.isInstanceOf[Tower])
        return
    target = newTarget
    this.updateTargetPos(true)
  }

  def updateTargetPos(forceUpdate: Boolean = false): Unit = {
    if(target.isEmpty)
      return
    if(target.get.isInstanceOf[Building] && !forceUpdate)
      return
    this.calculatePath(target.get.getPosition()._1, target.get.getPosition()._2)
  }

  override protected def attack(): Unit = {
    new Audio(Constants.GOBLIN_ATTACK_AUDIO).play()
    super.attack()
    if(target.isDefined){
      if(target.get.takeDamage(damage, this)){
        hasReachedTarget = false
        EntitiesManager.destroyEntity(target.get)
      }
    }
  }

  override def takeDamage(amount: Int, source: Entity): Boolean = {
    isStunned = true
    lastTimeStunned = GameManager.gameTimer
    val isDead = super.takeDamage(amount, source)
    if (!isDead) {
      new Audio(PLAYER_ATTACK_AUDIO).play()
      new Audio(Constants.GOBLIN_HURT_AUDIO).play()
    }
    else {
      new Audio(Constants.PLAYER_KILL_ATTACK_AUDIO).play()
      new Audio(Constants.GOBLIN_DEATH_AUDIO).play()
    }
    this.updateTarget(Some(source))
    isDead
  }

  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_ORC_IDLE,
    duration = 1000,
    loop = true,
    active = true
  ))

  this.addAnimation("hurt", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_ORC_HURT,
    duration = 300,
    loop = false,
    active = false
  ))

  this.addAnimation("attack1", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_ORC_ATTACK_1,
    duration = 200,
    loop = false,
    active = false
  ))

  this.addAnimation("walk", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_ORC_WALK,
    duration = 1000,
    loop = true,
    active = false
  ))

  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)
  this.playAnimation("idle")

  this.animations("hurt").onAnimationEnded(() => playAnimation("idle"))
  this.animations("attack1").onAnimationEnded(() => {
    if(!this.isAnimationPlaying())
      playAnimation("idle")
  })
  this.animations("attack1").onAnimationStarted(() => isAttacking = true)
  this.animations("attack1").onAnimationEnded(() => isAttacking = false)

  var enemyHealthBarSprite: Sprite = new Sprite(Constants.PLAYER_HP_BAR_1, this.getAbsPosition)
  Layers.addSprite(Constants.LAYER_UI_MOBILE, enemyHealthBarSprite)

}
