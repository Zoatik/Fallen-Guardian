class Entity(
              var pos: (Int, Int),          // Position de l'entité (x, y)
              var hp: Int,                  // Points de vie
              val sprite: Sprite,           // Sprite de l'entité
              val boxCollision2D: CollisionBox2D, // Boîte de collision associée
              var armor: Int
) {
  def isAlive: Boolean = hp > 0

  def takeDamage(amount: Int): Unit = {
    val effectiveAmount = Math.max(amount - armor, 0)
    hp -= effectiveAmount
    if (hp < 0) hp = 0
  }

  def setPosition(newPos: (Int, Int)): Unit = {
    pos = newPos
    val absolutePosX = Constants.CELL_SIZE * newPos._1
    val absolutePosY = Constants.CELL_SIZE * newPos._2
    sprite.setPosition(absolutePosX, absolutePosY)
    boxCollision2D.setPosition(absolutePosX, absolutePosY)
  }
}

