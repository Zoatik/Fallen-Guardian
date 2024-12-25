/**
 * Creates a Grid with the given sizes
 *
 * @param size      Size of the grid (number of cells x,y)
 * @param cellSize  Size of one cell in pixels
 */
class Grid(val size: (Int, Int), val cellSize: Int) {
  val cells: Array[Array[Cell]] = Array.ofDim(size._1, size._2)
  for(i <- cells.indices){
    for(j <- cells(0).indices){
      val cellPos: (Int, Int) = (cellSize * i, cellSize * j)
      cells(i)(j) = new Cell(cellPos, cellSize, CellStates.EMPTY)
    }
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
           var state: CellStates.CellState)
{
  val sprite: Sprite = new Sprite("/res/cellEmpty.png", pos)

}


object CellStates extends Enumeration {
  type CellState = Value

  val EMPTY, OCCUPIED, BLOCK_PATH = Value
}