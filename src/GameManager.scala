import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import hevs.graphics.FunGraphics
import InputManager._
import Constants._

import scala.collection.mutable

/**
 * Main Manager that handles the game logics
 */
object GameManager {
  // Window and Grid setup
  val fg = new FunGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, "Fallen Guardian", false)

  val camera2D: Camera2D = new Camera2D()
  var mouseX: Int = 0
  var mouseY: Int = 0
  var initialized: Boolean = false

  var waveCounter: Int = 0
  var isWavePlaying: Boolean = false


  /**
   * Initialize all necessary components
   */
  def init(): Unit = {
    /*---Grid initialisation---*/
    Grid.init((GRID_SIZE,GRID_SIZE), CELL_SIZE)
    for( el <- Grid.cells){ // Adds sprite to layer for each cell
      for(cell <- el){
        Layers.addSprite(LAYER_GROUND, cell.sprite)
      }
    }

    this.setInputListeners()
    fg.displayFPS(true)

    initialized = true
  }

  /**
   * ensures that the Game Manager is initialized before use
   * @throws IllegalStateException GameManager must be initialized using GameManager.init(params) before use.
   */
  private def ensureInitialized(): Unit = {
    if (!initialized) {
      throw new IllegalStateException("GameManager must be initialized using GameManager.init(params) before use.")
    }
  }


  def startWave(): Unit = {
    waveCounter += 1
    isWavePlaying = true
  }

  def stopWave(): Unit = {
    isWavePlaying = false
  }

  private def update(): Unit = {
    EntitiesManager.updateActions()
    if(isWavePlaying) {
      //EntitiesManager.updateSpawn()
    }
  }

  /**
   * Game loop
   *  - Calls InputManager handling functions
   *  - Calls Gamemanager.update function
   *  - Calls AnimationsManager.run function
   *  - Calls Renderer.render function
   *  - Update the deltaT
   */
  def loop(): Unit = {

    while (true) {
      InputManager.handleKeys()
      InputManager.handleMouse()

      GameManager.update()
      AnimationsManager.run()

      Renderer.render(fg)

    }
  }

  /**
   * Handles the cells Action (pressed or released)
   * @param mouseButton mouse button code that has been triggered
   * @param pressed     true if pressed, false if released
   * @param cell        the interacted cell
   */
  def handleCellAction(mouseButton: Int, pressed: Boolean, cell: Cell): Unit = {
    ensureInitialized()
    if (!pressed){
      if(mouseButton == MouseEvent.BUTTON1) {
        EntitiesManager.player.target = None
        EntitiesManager.player.calculatePath(cell.pos._1, cell.pos._2)

      }
    }
  }

  def handleEntityMouseAction(mouseButton: Int, pressed: Boolean, entity: Entity): Unit = {
    ensureInitialized()
    if (!pressed) {
      if (mouseButton == MouseEvent.BUTTON1) {
        entity match {
          case enemy: Enemy => EntitiesManager.player.setTarget(enemy)
          case _ =>
        }
      }
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
