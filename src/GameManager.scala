import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import hevs.graphics.FunGraphics
import InputManager._
import Constants._

/**
 * Main Manager that handles the game logics
 */
object GameManager {
  // Window and Grid setup
  val fg = new FunGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, "Fallen Guardian", false)

  var layers: Layers = new Layers(NUMBER_OF_LAYERS)
  val camera2D: Camera2D = new Camera2D()
  var mouseX: Int = 0
  var mouseY: Int = 0
  var deltaT: Long = 0
  var initialized: Boolean = false

  var aled: Player = new Player()
  layers.addSprite(1, aled.sprite)

  /* test animation */
  var anim: Animation = new Animation(aled.sprite, AnimationsResources.ANIM_SOLDIER_IDLE, 1000, true)
  anim.play()
  bindKey(KeyEvent.VK_C, (_, _) => anim.play())
  bindKey(KeyEvent.VK_V, (_, _) => anim.stop())

  /**
   * Initialize all necessary components
   */
  def init(): Unit = {
    /*---Grid initialisation---*/
    Grid.init((GRID_SIZE,GRID_SIZE), CELL_SIZE)
    for( el <- Grid.cells){ // Adds sprite to layer for each cell
      for(cell <- el){
        layers.layerArray(0).addSprite(cell.sprite)
      }
    }

    this.setInputListeners()
    fg.displayFPS(true)

    initialized = true
  }

  /**
   * ensures that the Game Manager is initialized before use
   */
  private def ensureInitialized(): Unit = {
    if (!initialized) {
      throw new IllegalStateException("GameManager must be initialized using GameManager.init(params) before use.")
    }
  }

  /**
   * Handles the cells Action (pressed or released)
   * @param mouseButton mouse button code that has been triggered
   * @param pressed     true if pressed, false if released
   * @param cell        the interacted cell
   */
  def handleCellAction(mouseButton: Int, pressed: Boolean, cell: Cell): Unit = {
    if (!pressed){
      if(mouseButton == MouseEvent.BUTTON3) {
        aled.calculatePath(cell.pos._1, cell.pos._2)
      }
    }
  }

  /**
   * Updates the CharacterMovements,
   */
  private def update() : Unit = {
    aled.moveToTarget()
  }

  /**
   * Game loop
   *  - Calls InputManager handle functions
   *  - Calls Gamemanager.update function
   *  - Calls AnimationsManager.run function
   *  - Calls Renderer.render function
   *  - Update the deltaT
   */
  def loop(): Unit = {
    var prevTime: Long = System.currentTimeMillis()
    while (true) {
      InputManager.handleKeys()
      InputManager.handleMouse()

      GameManager.update()
      AnimationsManager.run()

      Renderer.render(fg, layers)
      val currTime = System.currentTimeMillis()
      deltaT = currTime - prevTime
      prevTime = currTime
    }
  }

  /**
   * Links the FunGraphics Input listeners with custom functions
   *  - keyPressed - InputManager.handleKeyPressed
   *  - keyReleased - InputManager.handleKeyReleased
   *  - mousePressed - InputManager.handleMouseButtonPressed
   *  - mouseReleased - InputManager.handleMouseButtonReleased
   *  - mouseMoved - InputManager.handleMouseMoved
   *  - mouseDragged - mouseMoved
   */
  private def setInputListeners(): Unit = {
    fg.setKeyManager(new KeyAdapter() { // Will be called when a key has been pressed
      override def keyPressed(e: KeyEvent): Unit = {

        InputManager.handleKeyPressed(e.getKeyCode)
      }

      override def keyReleased(e: KeyEvent): Unit = {
        InputManager.handleKeyReleased(e.getKeyCode)
      }
    })

    fg.addMouseListener(new MouseAdapter {
      override def mousePressed(e: MouseEvent): Unit = {
        InputManager.handleMouseButtonPressed(e.getButton)
      }

      override def mouseReleased(e: MouseEvent): Unit = {
        InputManager.handleMouseButtonReleased(e.getButton)
      }
    })

    fg.addMouseMotionListener(new MouseAdapter() {
      override def mouseMoved(e: MouseEvent): Unit = {
        // Get the mouse position from the event
        mouseX = e.getX
        mouseY = e.getY
        InputManager.handleMouseMoved(mouseX - Renderer.offsetX, mouseY- Renderer.offsetY)
      }

      override def mouseDragged(e: MouseEvent): Unit = {
        mouseMoved(e)
      }
    })
  }

}
