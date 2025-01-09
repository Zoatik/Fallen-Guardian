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

  var enemies: mutable.ListBuffer[Enemy] = mutable.ListBuffer()

  var aled: Player = new Player()


  InputManager.bindKey(KeyEvent.VK_Q, (_, pressed) => if(!pressed) DEBUG_SPAWN_ENEMY())
  InputManager.bindKey(KeyEvent.VK_E, (_, pressed) => if(!pressed) destroyEntity(enemies.head))

  def DEBUG_SPAWN_ENEMY(): Unit = {
    val oscour: Enemy = new Enemy(
      _pos = (20,20),
      _hp = 2000,
      _baseImagePath = "/res/Characters/Enemy/Orc/idle/orcIdle_0.png",
      _velocity = 1,
      _damage = 1,
      _armor = 2
    )
    enemies += oscour
    oscour.calculatePath(50,50)
  }


  def destroyEntity(entity: Entity): Unit = {
    entity match {
      case e: Enemy =>
        enemies -= e
        aled.target = None
      case _ =>
        println("Unsupported entity type for destruction.")
    }
    entity.destroy()
  }

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

  /**
   * Handles the cells Action (pressed or released)
   * @param mouseButton mouse button code that has been triggered
   * @param pressed     true if pressed, false if released
   * @param cell        the interacted cell
   */
  def handleCellAction(mouseButton: Int, pressed: Boolean, cell: Cell): Unit = {
    if (!pressed){
      if(mouseButton == MouseEvent.BUTTON1) {
        cell.sprite.changeImage("/res/ground/TX_stone_0.png") // DEBUG
        aled.target = None
        aled.calculatePath(cell.pos._1, cell.pos._2)

      }
    }
  }

  def handleEntityMouseAction(mouseButton: Int, pressed: Boolean, entity: Entity): Unit = {
    if (!pressed) {
      if (mouseButton == MouseEvent.BUTTON1) {
        aled.setTarget(entity)
      }
    }
  }

  var prevTime: Long = 0
  /**
   * Updates the CharacterMovements,
   */
  private def update() : Unit = {
    if(System.currentTimeMillis() - prevTime > 100 && !aled.isAttacking) {
      aled.updateTargetPos()
      prevTime = System.currentTimeMillis()
    }
    aled.moveToTarget()
    enemies.foreach(_.moveToTarget())
    aled.targetReached()

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
