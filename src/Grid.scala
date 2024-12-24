
class Grid(val size: (Int, Int), val cellSize: Int) {
  val cells: Array[Array[Cell]] = Array.ofDim(size._1, size._2)
  for(i <- cells.indices){
    for(j <- cells(0).indices){
      val cellPos: (Int, Int) = (cellSize * i, cellSize * j)
      cells(i)(j) = new Cell(cellPos, cellSize, CellStates.EMPTY)
    }
  }
}

class Cell(val pos: (Int, Int),
           val size: Int,
           var state: CellStates.CellState)
{
  val sprite: Sprite = new Sprite("/res/cellTest.png")
}


object CellStates extends Enumeration {
  type CellState = Value

  val EMPTY, OCCUPIED, BLOCK_PATH = Value
}