import AnimationsResources._

import scala.collection.mutable

/**
 * Unique Grid object that contains cells
 */
object Grid {
  private var size: (Int, Int) = (10, 10)
  private var cellSize: Int = 10
  private var initialized: Boolean = false
  var cells: Array[Array[Cell]] = Array.ofDim(size._1, size._2)
  var highLightedCells: mutable.ListBuffer[Cell] = mutable.ListBuffer()

  var highlightOnMouse: Boolean = false

  /**
   * Initializes the Grid
   * @param size grid size
   * @param cellsize single cell size in pixels
   * @note Must be called before using the Grid
   */
  def init(size: (Int, Int), cellsize: Int): Unit = {
    val rand = new scala.util.Random
    this.size = size
    this.cellSize = cellsize


    cells = Array.ofDim(size._1, size._2)
    for(i <- cells.indices){
      for(j <- cells(0).indices){
        var baseImageBitmap: BetterGraphicsBitmap = BITMAP_CELL_GRASS_0
        val randNum: Int = rand.nextInt(100)
        if (randNum > 50 && randNum <= 80)
          baseImageBitmap = BITMAP_CELL_GRASS_1
        else if (randNum > 80 && randNum <= 90)
          baseImageBitmap = BITMAP_CELL_FLOWER_0

        val cellPos: (Int, Int) = (i , j)
        cells(i)(j) = new Cell(cellPos, cellSize, CellStates.EMPTY, baseImageBitmap)
      }
    }

    initialized = true
  }

  /**
   * Ensures that the grid is initialized
   * @throws IllegalStateException Grid must be initialized using Grid.init() before use.
   */
  private def ensureInitialized(): Unit = {
    if (!initialized) {
      throw new IllegalStateException("Grid must be initialized using Grid.init() before use.")
    }
  }

  def restoreHighlightedCells(): Unit = {
    highLightedCells.foreach(cell => cell.sprite.changeImage(cell.defaultImageBitmap))
  }

  /**
   * Retrieves a Cell from a point with absolute coordinates
   * @param x absolute position x
   * @param y absolute position y
   * @return The cell at this position or None if not found
   */
  def getCellFromPoint(x: Int, y: Int): Option[Cell] = {
    val i: Int = x / 32
    val j: Int = y / 32
    getCell(i,j)
  }

  /**
   *
   * @param i raw index of the cell
   * @param j column index of the cell
   * @return The cell at the given index or None if not found
   */
  def getCell(i: Int,j: Int): Option[Cell] = {
    if(!isInGrid(i,j))
      return None

    Some(this.cells(i)(j))
  }

  /**
   *
   * @param x raw index of the cell
   * @param y column index of the cell
   * @return true if the given address is valid, false otherwise
   */
  private def isInGrid(x: Int, y: Int): Boolean = {
    x >= 0 && y >= 0 && x < cells.length && y < cells(0).length
  }

  def build(cell: Cell, buildSelected: Int, lvl: Int): Unit = {
    cell.state = CellStates.BLOCK_PATH
    EntitiesManager.addBuilding(cell, buildSelected, lvl)
  }

  def removeBuilding(building: Building): Unit = {
    val cell = getCell(building.getPosition()._1, building.getPosition()._2).getOrElse(return)
    cell.state = CellStates.EMPTY
    cell.entityPlaced = None
    EntitiesManager.destroyEntity(building)
  }

  /*------------ PATH FINDING FUNCTIONS ------------*/


  /**
   * findPath function - public
   * @param start   start Cell
   * @param target  targeted Cell
   * @param ignoreTargetCollision if true, ignores the state of the cell target
   * @param ignoreCollisions if true, ignores the states of all cells
   * @return if path exists, returns an array of cells, otherwise None
   */
  def findPath(start: Cell, target: Cell, ignoreTargetCollision: Boolean = true, ignoreCollisions: Boolean = false): Option[Array[Cell]] = {
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
      for ((neighbor, cost) <- getNeighbours(current, target, ignoreTargetCollision, ignoreCollisions)) {
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

  def findPathToNextCollider(start: Cell, target: Cell): Option[Array[Cell]] = {
    val pathWithoutCollision: Option[Array[Cell]] = findPath(start, target, ignoreCollisions = true)
    var newPath: mutable.ListBuffer[Cell] = mutable.ListBuffer()
    if(pathWithoutCollision.nonEmpty){
      var stop: Boolean = false
      for(cell <- pathWithoutCollision.get if !stop){
        newPath += cell
        if(cell.state == CellStates.BLOCK_PATH)
          stop = true
      }
    }
    Some(newPath.toArray)
  }

  /**
   * Get the neighbours from a given cell
   *
   * @param cell the central cell
   * @return an Array of cells that are neighbours to the given cell
   */
  private def getNeighbours(cell: Cell, target: Cell, ignoreTargetCollision: Boolean, ignoreCollision: Boolean): Array[(Cell, Double)] = {
    val neighbours = mutable.ListBuffer[(Cell, Double)]()
    for (i <- -1 to 1; j <- -1 to 1) {
      if (!(i == 0 && j == 0)) { // Ignorer la cellule elle-même
        val otherX: Int = cell.pos._1  + i
        val otherY: Int = cell.pos._2  + j
        val cost: Double = if (i != 0 && j != 0) math.sqrt(2) else 1.0 // Diagonaux coûtent sqrt(2)
        if (isInGrid(otherX, otherY)) {
          if(ignoreCollision || cells(otherX)(otherY).state != CellStates.BLOCK_PATH) {
            neighbours += ((cells(otherX)(otherY), cost))
          }
          else if(ignoreTargetCollision && cells(otherX)(otherY) == target){
            neighbours += ((cells(otherX)(otherY), cost))
          }
        }
      }
    }
    neighbours.toArray
  }

  /**
   * Adds Cell to the path by reconstructing it
   * @param cameFrom  the previous Cell calculated
   * @param current   the current Cell
   * @return an Array of cells that create the path
   */
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
 * @param pos   top left position of the cell
 * @param size  size in pixels
 * @param state state of the cell : EMPTY, OCCUPIED, Block_PATH
 */
class Cell(val pos: (Int, Int),
           val size: Int,
           var state: CellStates.CellState,
           var defaultImageBitmap: BetterGraphicsBitmap = BITMAP_CELL_GRASS_0) {
  val absolutePos: (Int, Int) = (pos._1 * size, pos._2 * size)
  val sprite: Sprite = new Sprite(defaultImageBitmap, absolutePos)
  val box2D: Box = Box(absolutePos._1, absolutePos._2, size, size)
  val collisionBox: CollisionBox2D = CollisionBox2DManager.newCollisionBox2D(box2D, Constants.COLLISION_LAYER_GROUND)
  var entityPlaced: Option[Entity] = None

  collisionBox.onMouseEnter(() => startHover())
  collisionBox.onMouseLeave(() => endHover())
  //collisionBox.onMousePressed(mouseButton: Int => mousePressed(mouseButton))
  collisionBox.onMouseReleased(mouseButton => mouseReleased(mouseButton))


  /**
   * Defines the action to do when hovered
   */
  def startHover(): Unit = {
    if(Grid.highlightOnMouse) {
      Grid.highLightedCells += this
      this.sprite.changeImage(BITMAP_CELL_EMPTY)
    }
  }

  /**
   * Defines the action to do when end hovered
   */
  def endHover(): Unit = {
    if(Grid.highlightOnMouse) {
      Grid.highLightedCells -= this
      this.sprite.changeImage(defaultImageBitmap)
    }
  }

  /**
   * Defines the action to do when a mouse button is pressed
   * @param mouseButton mouse button keyCode
   */
  def mousePressed(mouseButton: Int): Unit = {
    println("pressed")
  }

  /**
   * Defines the action to do when a mouse button is released
   * @param mouseButton mouse button keyCode
   */
  def mouseReleased(mouseButton: Int): Unit = {
    GameManager.handleCellAction(mouseButton, pressed = false, this)
  }

  /**
   *
   * @param other other cell
   * @return distance from this cell to an other cell
   * @note distance is calculated with cell position in grid not absolute position
   */
  def distanceTo(other: Cell): Double = {
    val dx = math.abs(pos._1 - other.pos._1)
    val dy = math.abs(pos._2 - other.pos._2)
    math.sqrt(dx * dx + dy * dy) // Distance euclidienne
  }
}

/**
 * Enum of Cells states:
 *  - EMPTY       -> nothing on the cell
 *  - OCCUPIED    -> something on the cell
 *  - BLOCK_PATH  -> something that blocks navigation on the cell
 */
object CellStates extends Enumeration {
  type CellState = Value

  val EMPTY, OCCUPIED, BLOCK_PATH = Value
}