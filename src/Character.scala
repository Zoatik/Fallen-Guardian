class Character(
                 pos: (Int, Int),
                 hp: Int,
                 sprite: Sprite,
                 boxCollision2D: CollisionBox2D,
                 armor: Int,
                 var velocity: (Int, Int),    // Vitesse de déplacement (dx, dy)
                 var damage: Int             // Quantité de dégâts infligés
               ) extends Entity(pos, hp, sprite, boxCollision2D, armor) {

  def move(): Unit = {
    val newX = pos._1 + velocity._1
    val newY = pos._2 + velocity._2
    this.setPosition((newX, newY))
  }

  def attack(target: Entity): Unit = {
    target.takeDamage(damage)
  }
}

