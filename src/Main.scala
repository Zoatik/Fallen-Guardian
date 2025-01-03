import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.{Color, MouseInfo}
import hevs.graphics.FunGraphics
import Constants._
import InputManager._
import Renderer._

object Main extends App {

  // Window and Grid setup
  val fg = new FunGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, "Lost In Hell", false)

  Grid.init((GRID_SIZE,GRID_SIZE), CELL_SIZE)
  var layers: Layers = new Layers(3)
  var mouseX: Int = 0
  var mouseY: Int = 0
  for( el <- Grid.cells){ // Adds sprite for each cell
    for(cell <- el){
      layers.layerArray(0).addSprite(cell.sprite)
    }
  }

  // Camera behaviour
  bind(KeyEvent.VK_A, Renderer.move(10,0))
  bind(KeyEvent.VK_D, Renderer.move(-10,0))
  bind(KeyEvent.VK_S, Renderer.move(0,-10))
  bind(KeyEvent.VK_W, Renderer.move(0,10))
  bind(KeyEvent.VK_A, CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  bind(KeyEvent.VK_D, CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  bind(KeyEvent.VK_S, CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  bind(KeyEvent.VK_W, CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))

  // Input listeners
  fg.setKeyManager(new KeyAdapter() { // Will be called when a key has been pressed
    override def keyPressed(e: KeyEvent): Unit = {

      handleKeyPressed(e.getKeyCode)
    }

    override def keyReleased(e: KeyEvent): Unit = {
      handleKeyReleased(e.getKeyCode)
    }
  })

  fg.addMouseListener(new MouseAdapter {
    override def mousePressed(e: MouseEvent): Unit = {
      CollisionBox2DManager.handleMousePressed()
    }

    override def mouseReleased(e: MouseEvent): Unit = {
      CollisionBox2DManager.handleMouseReleased()
    }
  })

  fg.addMouseMotionListener(new MouseAdapter() {
    override def mouseMoved(e: MouseEvent): Unit = {
      // Get the mouse position from the event
      mouseX = e.getX
      mouseY = e.getY

      CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY)
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      mouseMoved(e)
    }
  })


  /** Main Loop **/
  while (true) {
    handleKeys()
    render(fg, layers)
  }
}









