class Entity(
              var pos: (Int, Int),          // Position de l'entité (x, y)
              var hp: Int,                  // Points de vie
              var armor: Int,
              val sprite: Sprite,           // Sprite de l'entité
              val boxCollision2D: CollisionBox2D // Boîte de collision associée

) {
  def isAlive: Boolean = hp > 0

  def takeDamage(amount: Int): Unit = {
    val effectiveAmount = Math.max(amount - armor, 0)
    hp -= effectiveAmount
    if (hp < 0) hp = 0
  }

  /**
   * Sets the retlative position to the grid
   * @param newPos new position relative to the grid
   */
  def setPosition(newPos: (Int, Int)): Unit = {
    pos = newPos
    val absolutePosX = Constants.CELL_SIZE * newPos._1
    val absolutePosY = Constants.CELL_SIZE * newPos._2
    sprite.setPosition(absolutePosX, absolutePosY)
    boxCollision2D.setPosition(absolutePosX, absolutePosY)
  }

  /**
   * Sets the absolute position in the 2D space (pixels)
   * @param newAbsPos new absolute position
   */
  def setAbsPosition(newAbsPos: (Int, Int)): Unit ={
    val newPos = (newAbsPos._1 / Constants.CELL_SIZE, newAbsPos._2 / Constants.CELL_SIZE)
    this.pos = newPos
    sprite.setPosition(newAbsPos._1, newAbsPos._2)
    boxCollision2D.setPosition(newAbsPos._1, newAbsPos._2)
  }

  /**
   *
   * @return Relative position to the grid
   */
  def getPosition(): (Int, Int) = pos

  /**
   *
   * @return Absolute position in 2D space (pixels)
   */
  def getAbsPosition(): (Int, Int) = (Constants.CELL_SIZE * this.pos._1, Constants.CELL_SIZE * this.pos._2)

}

