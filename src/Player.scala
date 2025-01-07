class Player(
              pos: (Int, Int),
              hp: Int,
              armor: Int,
              sprite: Sprite,
              boxCollision2D: CollisionBox2D,
              velocity: (Int, Int),
              damage: Int,
            ) extends Character(pos, hp, armor, sprite, boxCollision2D, velocity, damage) {

}
