import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.Color
import hevs.graphics.FunGraphics
import Constants._
import Renderer._

object Main extends App {

  /* Window and Grid setup */
  val fg = new FunGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, "Lost In Hell", false)
  val im = new InputManager()
  val grid: Grid = new Grid((GRID_SIZE,GRID_SIZE), 32)
  var layers: Layers = new Layers(3)
  for( el <- grid.cells){ // Adds sprite for each cell
    for(cell <- el){
      layers.layerArray(0).addSprite(cell.sprite)
    }
  }



  var counter: Int = 0
  var m: Int = 0
  var n: Int = 0

  private def cellTest(): Unit = {

    grid.cells(m)(n).sprite.changeImage("/res/cellTest2.png")
    counter += 1
    m = counter % (GRID_SIZE)
    n = counter / (GRID_SIZE)

  }

  im.bind(KeyEvent.VK_C, cellTest())

  fg.setKeyManager(new KeyAdapter() { // Will be called when a key has been pressed
    override def keyPressed(e: KeyEvent): Unit = {
      im.handleKeyPressed(e.getKeyCode)
    }

    /*override def keyReleased(e: KeyEvent): Unit = {
      im.handleKeyReleased()
    }*/
  })
  fg.addMouseMotionListener(new MouseAdapter() {
    override def mouseMoved(e: MouseEvent): Unit = {
      val event = e

      // Get the mouse position from the event
      val posx = event.getX
      val posy = event.getY

      CollisionBox2DManager.checkMouseCollisions(posx, posy)
    }
  })


  /** Main Loop **/
  while (true) {

    render(fg, layers)
  }
}









