
class Base(
           _pos: (Int, Int),
           _hp: Int,
           _armor: Int ,
           _lvl: Int = 1,
           _baseImagePath: String ,
           _spriteAnchor: Int
           ) extends Entity(_pos, _hp, _armor, _lvl, _baseImagePath, _spriteAnchor) {


  for(i <- 0 until 3; j <- 0 until 3){
    Grid.getCell(this.pos._1 + i, this.pos._2 + j).get.state = CellStates.BLOCK_PATH
  }

  def this() = this(
    _pos = Constants.BASE_DEFAULT_POS,
    _hp = Constants.BASE_DEFAULT_HP,
    _armor = Constants.BASE_DEFAULT_ARMOR,
    _lvl = 1,
    _baseImagePath = Constants.BASE_DEFAULT_IMAGE_PATH,
    _spriteAnchor = Constants.ANCHOR_TOP_LEFT
  )

  override def takeDamage(amount: Int, source: Entity): Boolean = {
    val isDead: Boolean = super.takeDamage(amount, source)
    playAnimation("hurt")
    isDead
  }

  this.addAnimation("hurt", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_BASE_HURT,
    duration = 1000,
    loop = false,
    active = false
  ))

  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)
}