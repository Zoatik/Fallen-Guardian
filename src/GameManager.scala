import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import hevs.graphics.FunGraphics
import Constants._


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
  var prevWaveEndedTime: Long = 0
  var isWavePlaying: Boolean = false
  var prevTime: Long = System.currentTimeMillis()
  var gameTimer: Long = 0
  var isPaused: Boolean = false
  var isReadyToStart: Boolean = false


  InputManager.bindKey(KeyEvent.VK_B, (_, pressed) => if(!pressed) changeGameMode())
  InputManager.bindKey(KeyEvent.VK_ESCAPE, (_, pressed) => if(!pressed) isPaused = !isPaused)
  InputManager.bindKey(KeyEvent.VK_ENTER, (_, pressed) => if(!pressed) isReadyToStart = true)

  private def changeGameMode(): Unit = {
    EntitiesManager.player.get.isBuilding = !EntitiesManager.player.get.isBuilding
    Grid.highlightOnMouse = EntitiesManager.player.get.isBuilding
    Grid.restoreHighlightedCells()
  }


  /**
   * Initialize all necessary components
   */
  def init(): Unit = {
    /*---Grid initialisation---*/
    Grid.init((GRID_SIZE,GRID_SIZE), CELL_SIZE)
    for(el <- Grid.cells){ // Adds sprite to layer for each cell
      for(cell <- el){
        Layers.addSprite(LAYER_GROUND, cell.sprite)
      }
    }
    camera2D.setPosition(GRID_SIZE*CELL_SIZE/2,GRID_SIZE*CELL_SIZE/2)

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
    EntitiesManager.startWave(waveCounter)
  }


  def stopWave(): Unit = {
    isWavePlaying = false
    prevWaveEndedTime = gameTimer
  }

  private def update(): Unit = {
    if(isWavePlaying) {
      isWavePlaying = EntitiesManager.updateWave()
      if(!isWavePlaying)
        stopWave()
    }
    else if(GameManager.gameTimer - prevWaveEndedTime > WAVE_PAUSE_TIME && isReadyToStart){
      startWave()
    }

    EntitiesManager.updateActions()
  }

  def gameOver(win: Boolean): Unit = {
    isWavePlaying = false
    if(win){
      println("YOU WON")
    }
    else{
      println("YOU LOST")
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

      if(!isPaused) {
        gameTimer += System.currentTimeMillis() - prevTime
        prevTime = System.currentTimeMillis()
        GameManager.update()
      }
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
    if(isPaused)
      return
    if (!pressed){
      if(EntitiesManager.player.get.isBuilding){
        if (mouseButton == MouseEvent.BUTTON1 && cell.state == CellStates.EMPTY) {
          EntitiesManager.player.get.build(cell)
        }
        else if(mouseButton == MouseEvent.BUTTON3){
          changeGameMode()
        }
      }

      else {
        if (mouseButton == MouseEvent.BUTTON1) {
          EntitiesManager.player.get.target = None
          EntitiesManager.player.get.isAttacking = false
          EntitiesManager.player.get.calculatePath(cell.pos._1, cell.pos._2)

        }
      }
    }
  }

  def handleEntityMouseAction(mouseButton: Int, pressed: Boolean, entity: Entity): Unit = {
    ensureInitialized()
    if(isPaused)
      return
    if (!pressed) {
      if(EntitiesManager.player.get.isBuilding) {
        if (mouseButton == MouseEvent.BUTTON3) {
          entity match {
            case building: Building => EntitiesManager.player.get.sell(building)
          }
        }
        else if (mouseButton == MouseEvent.BUTTON1){
          entity match {
            case building: Building => EntitiesManager.player.get.upgrade(building)
          }
        }
      }
      else {
        if (mouseButton == MouseEvent.BUTTON1) {
          entity match {
            case enemy: Enemy => EntitiesManager.player.get.setTarget(enemy)
            case _ =>
          }
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
