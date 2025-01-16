
class Tower(
             _pos: (Int, Int),
             _maxHp: Int,
             _armor: Int,
             _lvl: Int,
             _baseImageBitmap: BetterGraphicsBitmap,
             _price: Int,
             _blockPath: Boolean,
             _isTargetable: Boolean,
             var damage: Int,
             var range: Int,
             var attackSpeed: Double,
             var target: Option[Enemy] = None
           ) extends Building(_pos, _maxHp, _armor, _lvl, _baseImageBitmap, _price, _blockPath, _isTargetable) {


  def this(pos: (Int, Int), lvl: Int) = this(
    _pos = pos,
    _maxHp = Constants.TOWER_DEFAULT_HP,
    _armor = Constants.TOWER_DEFAULT_ARMOR,
    _lvl = lvl,
    _baseImageBitmap = Constants.TOWER_DEFAULT_IMAGE_BITMAP,
    _price = Constants.BUILD_TOWER_PRICE,
    _blockPath = true,
    _isTargetable = true,
    damage = Constants.TOWER_DEFAULT_DAMAGE * lvl,
    range = Constants.TOWER_DEFAULT_RANGE * (1 + lvl / 5),
    attackSpeed = Constants.TOWER_DEFAULT_ATTACK_SPEED * (1 + lvl/10.0)
  )

  override def levelUp(): Unit = {
    new Audio(Constants.TOWER_LEVEL_UP_AUDIO).play()
    super.levelUp()
    maxHp += maxHp/5
    hp = maxHp
    damage = Constants.TOWER_DEFAULT_DAMAGE * lvl
    range = Constants.TOWER_DEFAULT_RANGE * (1 + lvl / 5)
    attackSpeed = Constants.TOWER_DEFAULT_ATTACK_SPEED / (2 + lvl)
  }

  override def destroy(): Unit = {
    super.destroy()
    Renderer.destroy(towerHealthBarSprite)
  }

  def updateTarget(): Unit = {
    if (target.isDefined) {
      return
    }
    this.target = findTarget()
  }

  override def findTarget(): Option[Enemy] = {
    if(target.isEmpty) {
      var minDist: Double = Double.PositiveInfinity
      var closestTarget: Option[Enemy] = None
      EntitiesManager.enemies.foreach(enemy => {
        val currentDist: Double = this.absDistanceTo(enemy)
        if (currentDist < minDist) {
          minDist = currentDist
          closestTarget = Some(enemy)
        }
      })
      if (minDist > range) {
        closestTarget = None
      }
      return closestTarget
    }
    target
  }

  def towerTryToAttack(): Unit = {
    if(target.isEmpty)
      target = findTarget()
    if (target.isDefined) {
      towerAttack()
    }
  }

  private var prevTowerAttackTime: Double = 0
  protected def towerAttack(): Unit = {
    if(target.isDefined && ( GameManager.gameTimer - prevTowerAttackTime )>= attackSpeed){
      EntitiesManager.spawnBullet((0, -1), this)
      prevTowerAttackTime = GameManager.gameTimer
    }
  }

  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesBitmapArray = AnimationsResources.ANIM_TOWER_IDLE,
    duration = 1000,
    loop = true,
    active = true
  ))


  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)
  this.playAnimation("idle")

  var towerHealthBarSprite: Sprite = new Sprite(Constants.TOWER_HP_BAR_1, this.getAbsPosition)
  Layers.addSprite(Constants.LAYER_UI_MOBILE, towerHealthBarSprite)

  new Audio(Constants.TOWER_PLACEMENT_AUDIO).play()
  new Audio(Constants.SELL_AUDIO).play()
}
