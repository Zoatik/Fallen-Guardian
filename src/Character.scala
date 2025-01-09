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

  var isAttacking: Boolean = false
  private var hasReachedTarget: Boolean = false
  private val attackCooldown: Int = 1000
  private var prevAttackTime: Long = 0
  var target: Option[Entity] = None


  override def destroy(): Unit = {
    super.destroy()
    pathQueue.empty
    target = None
  }

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

      if(this.pathQueue.isEmpty) {
        if(dist < velocity * 3) {
          this.setAbsPosition(nextStep)
          this.stopMoving()
          if(target.isDefined) {
            hasReachedTarget = true
            targetReached()
          }

          return true
        }
      }
      else
        nextStep = this.pathQueue.dequeue().absolutePos
    }
    dist = this.absDistanceTo(this.nextStep)

    val direction: (Int, Int) = (nextStep._1 - this.getAbsPosition._1, nextStep._2 - this.getAbsPosition._2)
    val length: Double = math.sqrt(math.pow(direction._1.toDouble, 2) + math.pow(direction._2.toDouble, 2))
    val normDirection: (Int, Int) = ((direction._1/ length * velocity * (1 + Renderer.deltaT/10)).toInt,
                                      (direction._2/length * velocity * (1 + Renderer.deltaT/10)).toInt)
    var expectedPos: (Int, Int) = (this.getAbsPosition._1 + normDirection._1, this.getAbsPosition._2 + normDirection._2)
    var newDist: Double = math.sqrt(math.pow(nextStep._1 - expectedPos._1, 2) + math.pow(nextStep._2 - expectedPos._2, 2))
    var scaledNormDirection: (Int, Int) = normDirection

    var i: Int = 1
    while(newDist >= dist && i < 10){
      scaledNormDirection = (normDirection._1 / i, normDirection._2 / i)
      expectedPos = (this.getAbsPosition._1 + scaledNormDirection._1, this.getAbsPosition._2 + scaledNormDirection._2)
      newDist = math.sqrt(math.pow(nextStep._1 - expectedPos._1, 2) + math.pow(nextStep._2 - expectedPos._2, 2))
      println(i)
      i += 1
    }
    this.move(scaledNormDirection._1 / i , scaledNormDirection._2 / i)

    hasReachedTarget = false
    false
  }

  def calculatePath(posX: Int, posY: Int): Unit = {
    pathQueue.removeAll()
    val startCell: Cell = Grid.getCell(this.pos._1, this.pos._2).getOrElse(Grid.getCell(0,0).get)
    val targetCell: Cell = Grid.getCell(posX, posY).getOrElse(Grid.getCell(0,0).get)
    for(cell <- Grid.findPath(startCell, targetCell).getOrElse(Array.empty)){
      this.pathQueue.enqueue(cell)
    }
    nextStep = this.pathQueue.dequeue().absolutePos
    if(pathQueue.isEmpty)
      return
    this.startMoving()
  }

  def startMoving(): Unit = {
    this.playAnimation("walk")
    isMoving = true
  }

  def stopMoving(): Unit = {
    this.playAnimation("idle")
    isMoving = false
  }

  def targetReached(): Unit = {
    if (target.isDefined && hasReachedTarget){
      if(System.currentTimeMillis() - prevAttackTime > attackCooldown) {
        attack()
        prevAttackTime = System.currentTimeMillis()
      }
    }
  }

  def attack(): Unit = {
    if(target.isDefined) {
      isAttacking = true
      playAnimation("attack1")
      target.get.takeDamage(damage)
      println("attack")
    }
  }

  def setVelocity(newVelocity: Int): Unit = this.velocity = newVelocity
  def getVelocity: Int = this.velocity
}

