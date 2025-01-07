class Enemy(
             pos: (Int, Int),
             hp: Int,
             sprite: Sprite,
             boxCollision2D: CollisionBox2D,
             velocity: (Int, Int),
             damage: Int,
             armor: Int
           ) extends Character(pos, hp, armor, sprite, boxCollision2D, velocity, damage) {

}
