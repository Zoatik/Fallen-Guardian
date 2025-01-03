import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.{Color, MouseInfo}
import hevs.graphics.FunGraphics
import Constants._
import Renderer._

object Main extends App {

  // Window and Grid setup
  val fg = new FunGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, "Lost In Hell", false)
  val im = new InputManager()
  val grid: Grid = new Grid((GRID_SIZE,GRID_SIZE), 32)
  var layers: Layers = new Layers(3)
  var mouseX: Int = 0
  var mouseY: Int = 0
  for( el <- grid.cells){ // Adds sprite for each cell
    for(cell <- el){
      layers.layerArray(0).addSprite(cell.sprite)
    }
  }

  // Camera behaviour
  im.bind(KeyEvent.VK_A, Renderer.move(10,0))
  im.bind(KeyEvent.VK_D, Renderer.move(-10,0))
  im.bind(KeyEvent.VK_S, Renderer.move(0,-10))
  im.bind(KeyEvent.VK_W, Renderer.move(0,10))
  im.bind(KeyEvent.VK_A, CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  im.bind(KeyEvent.VK_D, CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  im.bind(KeyEvent.VK_S, CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  im.bind(KeyEvent.VK_W, CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))

  // Input listeners
  fg.setKeyManager(new KeyAdapter() { // Will be called when a key has been pressed
    override def keyPressed(e: KeyEvent): Unit = {

      im.handleKeyPressed(e.getKeyCode)
    }

    override def keyReleased(e: KeyEvent): Unit = {
      im.handleKeyReleased(e.getKeyCode)
    }
  })

  fg.addMouseMotionListener(new MouseAdapter() {
    override def mouseMoved(e: MouseEvent): Unit = {
      val event = e

      // Get the mouse position from the event
      mouseX = event.getX
      mouseY = event.getY

      CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY)
    }
  })


  /** Main Loop **/
  while (true) {
    im.handleKeys()
    render(fg, layers)
  }
}









