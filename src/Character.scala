class Character(
                 pos: (Int, Int),
                 hp: Int,
                 armor: Int,
                 sprite: Sprite,
                 boxCollision2D: CollisionBox2D,
                 var velocity: (Int, Int),    // Vitesse de déplacement (dx, dy)
                 var damage: Int             // Quantité de dégâts infligés
               ) extends Entity(pos, hp, armor, sprite, boxCollision2D) {

  def move(): Unit = {
    val newX = pos._1 + velocity._1
    val newY = pos._2 + velocity._2
    this.setPosition((newX, newY))
  }

  def attack(target: Entity): Unit = {
    target.takeDamage(damage)
  }

  def setVelocity(newVelocity: (Int, Int)): Unit = this.velocity = newVelocity
  def getVelocity: (Int, Int) = this.velocity
}

