import scala.collection.mutable


/**
 * Creates a Grid with the given sizes
 *
 * @param size      Size of the grid (number of cells x,y)
 * @param cellSize  Size of one cell in pixels
 */
object Grid {
  private var size: (Int, Int) = (10, 10)
  private var cellSize: Int = 10
  private var initialized: Boolean = false
  var cells: Array[Array[Cell]] = Array.ofDim(size._1, size._2)
  def init(size: (Int, Int), cellsize: Int): Unit = {
    this.size = size
    this.cellSize = cellsize

    cells = Array.ofDim(size._1, size._2)
    for(i <- cells.indices){
      for(j <- cells(0).indices){
        val cellPos: (Int, Int) = (cellSize * i, cellSize * j)
        cells(i)(j) = new Cell(cellPos, cellSize, CellStates.EMPTY)
      }
    }

    initialized = true
  }

  private def ensureInitialized(): Unit = {
    if (!initialized) {
      throw new IllegalStateException("Grid must be initialized using Grid.init() before use.")
    }
  }



  private def isInGrid(x: Int, y: Int): Boolean = {
    x >= 0 && y >= 0 && x < cells.length && y < cells(0).length
  }

  // Méthode pour obtenir les voisins valides d'une cellule
  private def getNeighbours(cell: Cell): Array[(Cell, Double)] = {
    val neighbours = mutable.ListBuffer[(Cell, Double)]()
    for (i <- -1 to 1; j <- -1 to 1) {
      if (!(i == 0 && j == 0)) { // Ignorer la cellule elle-même
        val otherX: Int = cell.pos._1 / cell.size + i
        val otherY: Int = cell.pos._2 / cell.size + j
        val cost: Double = if (i != 0 && j != 0) math.sqrt(2) else 1.0 // Diagonaux coûtent sqrt(2)
        if (isInGrid(otherX, otherY) && cells(otherX)(otherY).state != CellStates.BLOCK_PATH) {
          neighbours += ((cells(otherX)(otherY), cost))
        }
      }
    }
    neighbours.toArray
  }

  def findPath(start: Cell, target: Cell): Option[Array[Cell]] = {
    ensureInitialized()
    // Open list (nœuds à explorer) avec priorité
    val openSet: mutable.PriorityQueue[(Cell, Double)] =
      mutable.PriorityQueue.empty[(Cell, Double)](Ordering.by[(Cell, Double), Double](_._2).reverse)
    openSet.enqueue((start, 0.0)) // Ajouter le point de départ avec un coût de 0

    // Map pour suivre les parents (pour reconstruire le chemin)
    val cameFrom = mutable.Map[Cell, Cell]()

    // Map pour suivre les coûts de chaque cellule
    val gScore = mutable.Map[Cell, Double]().withDefaultValue(Double.PositiveInfinity)
    gScore(start) = 0.0

    // Map pour suivre le coût total estimé (f = g + h)
    val fScore = mutable.Map[Cell, Double]().withDefaultValue(Double.PositiveInfinity)
    fScore(start) = start.distanceTo(target)

    while (openSet.nonEmpty) {
      val (current, _) = openSet.dequeue()

      // Si le nœud actuel est la cible, reconstruire le chemin
      if (current == target) {

        return Some(reconstructPath(cameFrom, current))
      }

      // Explorer les voisins
      for ((neighbor, cost) <- getNeighbours(current)) {
        val tentativeGScore = gScore(current) + cost

        if (tentativeGScore < gScore(neighbor)) {
          // Mise à jour des scores
          cameFrom(neighbor) = current
          gScore(neighbor) = tentativeGScore
          fScore(neighbor) = tentativeGScore + neighbor.distanceTo(target)

          // Ajouter le voisin à l'open set si ce n'est pas déjà le cas
          if (!openSet.exists(_._1 == neighbor)) {
            openSet.enqueue((neighbor, fScore(neighbor)))
          }
        }
      }
    }

    // Si on atteint ici, aucun chemin n'a été trouvé
    None
  }

  // Méthode pour reconstruire le chemin à partir de `cameFrom`
  private def reconstructPath(cameFrom: mutable.Map[Cell, Cell], current: Cell): Array[Cell] = {
    var path = Array(current)
    var node = current
    while (cameFrom.contains(node)) {
      node = cameFrom(node)
      path = node +: path
    }
    path
  }
}

/**
 * Creates a Cell
 *
 * @param pos   top left position of the cell
 * @param size  size in pixels
 * @param state state of the cell : EMPTY, OCCUPIED, Block_PATH
 */
class Cell(val pos: (Int, Int),
           val size: Int,
           var state: CellStates.CellState) {
  val sprite: Sprite = new Sprite("/res/cellEmpty.png", pos)
  val box2D: Box = new Box(pos._1, pos._2, size, size)
  val collisionBox: CollisionBox2D = CollisionBox2DManager.newCollisionBox2D(box2D)
  collisionBox.onMouseEnter(() => startHover())
  collisionBox.onMouseLeave(() => endHover())
  collisionBox.onMousePressed(() => mousePressed())
  collisionBox.onMouseReleased(() => mouseReleased())


  def startHover(): Unit = {
    this.sprite.changeImage("/res/cellEmpty_2.png")
  }

  def endHover(): Unit = {
    this.sprite.changeImage("/res/cellEmpty.png")
  }

  def mousePressed(): Unit = {
    this.sprite.changeImage("/res/cellTest.png")
    val path = Grid.findPath(this, Grid.cells(2)(2))
    if (path.isDefined)
      for (el <- path.get) {
        el.sprite.changeImage("/res/cellTest.png")
      }
  }

  def mouseReleased(): Unit = {
    this.sprite.changeImage("/res/cellEmpty.png")
  }

  def distanceTo(other: Cell): Double = {
    val dx = math.abs(pos._1 - other.pos._1)
    val dy = math.abs(pos._2 - other.pos._2)
    math.sqrt(dx * dx + dy * dy) // Distance euclidienne
  }
}


object CellStates extends Enumeration {
  type CellState = Value

  val EMPTY, OCCUPIED, BLOCK_PATH = Value
}