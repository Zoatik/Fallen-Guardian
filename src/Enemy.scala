class Enemy(
             _pos: (Int, Int),
             _hp: Int,
             _baseImagePath: String,
             _velocity: Int,
             _damage: Int,
             _armor: Int
           ) extends Character(_pos, _hp, _armor, _baseImagePath, _velocity, _damage) {

  this.addAnimation("idle", new Animation(
    spriteTarget = this.sprite,
    imagesPathBuffer = AnimationsResources.ANIM_ORC_IDLE,
    duration = 1000,
    loop = true,
    active = true
  ))

  Layers.addSprite(Constants.LAYER_ENTITIES, this.sprite)
  this.playAnimation("idle")
}
