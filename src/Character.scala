import scala.collection.mutable

class Character(
                 _pos: (Int, Int),
                 _hp: Int,
                 _armor: Int,
                 _baseImagePath: String,
                 var velocity: Int,    // Vitesse de déplacement (dx, dy)
                 var damage: Int             // Quantité de dégâts infligés
               ) extends Entity(_pos, _hp, _armor, _baseImagePath) {

  private val pathQueue: mutable.Queue[Cell] = mutable.Queue()
  private var nextStep: (Int, Int) = this.getAbsPosition
  private var isMoving: Boolean = false

  private def move(deltaX: Int, deltaY: Int): Unit = {
    val newX = this.getAbsPosition._1 + deltaX
    val newY = this.getAbsPosition._2 + deltaY
    this.setAbsPosition((newX, newY))
  }

  def moveToTarget(): Boolean = {
    if(!isMoving)
      return true
    var dist = this.absDistanceTo(this.nextStep)
    if (dist < Constants.CELL_SIZE){

      if(this.pathQueue.isEmpty ) {
        if(dist < 10) {
          this.setAbsPosition(nextStep)
          isMoving = false
          return true
        }
      }
      else
        nextStep = this.pathQueue.dequeue().absolutePos
    }
    dist = this.absDistanceTo(this.nextStep)

    val direction: (Int, Int) = (nextStep._1 - this.getAbsPosition._1, nextStep._2 - this.getAbsPosition._2)
    val length: Double = math.sqrt(math.pow(direction._1.toDouble, 2) + math.pow(direction._2.toDouble, 2))
    val normDirection: (Int, Int) = ((direction._1/ length * velocity * (1 + GameManager.deltaT/10)).toInt,
                                      (direction._2/length * velocity * (1 + GameManager.deltaT/10)).toInt)
    var expectedPos: (Int, Int) = (this.getAbsPosition._1 + normDirection._1, this.getAbsPosition._2 + normDirection._2)
    var newDist: Double = math.sqrt(math.pow(nextStep._1 - expectedPos._1, 2) + math.pow(nextStep._2 - expectedPos._2, 2))
    var scaledNormDirection: (Int, Int) = normDirection

    var i: Int = 1
    while(newDist >= dist || i > 100){
      scaledNormDirection = (normDirection._1 / i, normDirection._2 / i)
      expectedPos = (this.getAbsPosition._1 + scaledNormDirection._1, this.getAbsPosition._2 + scaledNormDirection._2)
      newDist = math.sqrt(math.pow(nextStep._1 - expectedPos._1, 2) + math.pow(nextStep._2 - expectedPos._2, 2))

      i += 1
    }
    this.move(normDirection._1 / i , normDirection._2 / i)


    false
  }

  def calculatePath(posX: Int, posY: Int): Unit = {
    pathQueue.removeAll()
    val startCell: Cell = Grid.getCell(this.pos._1, this.pos._2).getOrElse(Grid.getCell(0,0).get)
    val targetCell: Cell = Grid.getCell(posX, posY).getOrElse(Grid.getCell(0,0).get)
    for(cell <- Grid.findPath(startCell, targetCell).getOrElse(Array.empty)){
      this.pathQueue.enqueue(cell)
      cell.sprite.changeImage("/res/ground/TX_stone_0.png")
    }
    nextStep = this.pathQueue.dequeue().absolutePos
    isMoving = true
  }


  def attack(target: Entity): Unit = {
    target.takeDamage(damage)
  }

  def setVelocity(newVelocity: Int): Unit = this.velocity = newVelocity
  def getVelocity: Int = this.velocity
}

