import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.{Color, MouseInfo}
import hevs.graphics.FunGraphics
import Constants._
import InputManager._
import Renderer._

import java.time.Instant
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Main extends App {

  // Window and Grid setup
  val fg = new FunGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, "Lost In Hell", false)

  Grid.init((GRID_SIZE,GRID_SIZE), CELL_SIZE)
  var layers: Layers = new Layers(3)
  val camera2D: Camera2D = new Camera2D
  var mouseX: Int = 0
  var mouseY: Int = 0
  var deltaT: Long = 0
  for( el <- Grid.cells){ // Adds sprite for each cell
    for(cell <- el){
      layers.layerArray(0).addSprite(cell.sprite)
    }
  }

  // Camera behaviour
  bindKey(KeyEvent.VK_A, isPressed => CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  bindKey(KeyEvent.VK_D, isPressed => CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  bindKey(KeyEvent.VK_S, isPressed => CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))
  bindKey(KeyEvent.VK_W, isPressed => CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY))

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
      handleMouseButtonPressed(e.getButton)
    }

    override def mouseReleased(e: MouseEvent): Unit = {
      handleMouseButtonReleased(e.getButton)
    }
  })

  fg.addMouseMotionListener(new MouseAdapter() {
    override def mouseMoved(e: MouseEvent): Unit = {
      // Get the mouse position from the event
      mouseX = e.getX
      mouseY = e.getY
      handleMouseMoved(mouseX - Renderer.offsetX, mouseY- Renderer.offsetY)
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      mouseMoved(e)
    }
  })

  """
  /** TEST Animation * */
  var images = AnimationsResources.ANIM_SOLDIER_IDLE
  val testSprite: Sprite = new Sprite(images.head, (defaultPos._1, defaultPos._2))
  layers.addSprite(1, testSprite)
  var anim: Animation = new Animation(testSprite, images, 1000, true)

  anim.play()
  bindKey(KeyEvent.VK_C, pressed => anim.play())
  bindKey(KeyEvent.VK_V, pressed => anim.stop())
  """


  // Player initial spawn (found out that I don't actually need it)
  //GameManager.spawnPlayer()
  //GameManager.aled()


  /** Main Loop **/
  fg.displayFPS(true)
  var prevTime: Long = System.currentTimeMillis()
  while (true) {
    handleKeys()
    handleMouse()

    GameManager.update()
    AnimationsManager.run()
    //GameManager.changeAled()

    render(fg, layers)
    val currTime = System.currentTimeMillis()
    deltaT = currTime - prevTime
    prevTime = currTime
  }
}









