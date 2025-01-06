class Enemy(
             pos: (Int, Int),
             hp: Int,
             sprite: Sprite,
             boxCollision2D: CollisionBox2D,
             velocity: (Int, Int),
             damage: Int,
             armor: Int
           ) extends Character(pos, hp, sprite, boxCollision2D, armor, velocity, damage) {

  def follow(target: Entity): Unit = {
    val dx = Math.signum(target.pos._1 - pos._1).toInt
    val dy = Math.signum(target.pos._2 - pos._2).toInt
    super.velocity = (dx, dy)
    move()
  }
}
