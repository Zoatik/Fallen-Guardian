class Player(
              pos: (Int, Int),
              hp: Int,
              sprite: Sprite,
              boxCollision2D: CollisionBox2D,
              velocity: (Int, Int),
              damage: Int,
              armor: Int
            ) extends Character(pos, hp, sprite, boxCollision2D, armor, velocity, damage) {

}
