package scripts.managers

import scripts.gameClasses._
import scripts.data.Constants._
import scripts.data.{AnimationsResources, Constants}
import scripts.managers.UiManager.StaticUiElement
import scripts.tools._

import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.{Color, Cursor}
import javax.sound.sampled.Clip.LOOP_CONTINUOUSLY

/**
 * Main Manager that handles the game logics
 */
object GameManager {
  val fg = new BetterFunGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, "Fallen Guardian", false)

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
  var isGameOver: Boolean = false

  InputManager.bindKey(KeyEvent.VK_Q, (_, pressed) => if(!pressed) changeGameMode())
  InputManager.bindKey(KeyEvent.VK_ESCAPE, (_, pressed) => if(!pressed) isPaused = !isPaused)
  InputManager.bindKey(KeyEvent.VK_ENTER, (_, pressed) => if(!pressed) ready())

  private def changeGameMode(): Unit = {

    val player: Player = EntitiesManager.player.getOrElse(return)
    player.isBuilding = !player.isBuilding
    Grid.highlightOnMouse = player.isBuilding
    Grid.restoreHighlightedCells()
    if(player.isBuilding) {
      if(player.canAffordBuild())
        changeCursor(CURSOR_BUILD)
      else
        changeCursor(CURSOR_NO_BUILD)
    }
    else
      changeCursor(CURSOR_DEFAULT)
  }

  /**
   * Initialize all necessary components
   */
  def init(): Unit = {
    /*---Grid initialisation---*/
    Grid.init((GRID_SIZE,GRID_SIZE), CELL_SIZE)
    for(el <- Grid.cells){
      for(cell <- el){
        Layers.addSprite(LAYER_GROUND, cell.sprite)
      }
    }
    camera2D.setPosition(GRID_SIZE*CELL_SIZE/2,GRID_SIZE*CELL_SIZE/2)
    changeCursor(CURSOR_DEFAULT)

    this.setInputListeners()
    this.createHUD()
    BetterAudio.playNewAudio("main theme",new BetterAudio(Constants.THEME_SONG_CHILL), LOOP_CONTINUOUSLY)

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

  private def changeCursor(cursor: Cursor): Unit = {
    fg.mainFrame.getComponent(0).setCursor(cursor)
  }

  private def createHUD(): Unit = {
    /** BASE LIFE BAR */
    val baseLifeBarSprite: Sprite = new Sprite(Constants.UI_BASE_LIFE_100,(WINDOW_WIDTH/2, 40))
    val baseLifeBar = UiManager.createUiElement(baseLifeBarSprite, LAYER_STATIC_UI_0, color = Color.WHITE)
    baseLifeBar.addAnimation("lowLife", new Animation(
      spriteTarget = baseLifeBar.sprite,
      imagesBitmapArray = AnimationsResources.ANIM_UI_BASE_LOW,
      duration = 400,
      loop = true,
      active = false
    ))
    baseLifeBar.addLogic(() => {
      var baseHp = 0
      if(EntitiesManager.base.isDefined) {
        val base: Base = EntitiesManager.base.get
        baseHp = base.getHp
        if(baseHp > base.getMaxHp * 0.25 && baseLifeBar.isAnimationPlaying("lowLife"))
          baseLifeBar.stopAnimation("lowLife")
        if(baseHp > base.getMaxHp * 0.75 && base.sprite.bm != UI_BASE_LIFE_100)
          baseLifeBar.sprite.changeImage(UI_BASE_LIFE_100)
        else if(baseHp > base.getMaxHp * 0.5 && base.sprite.bm != UI_BASE_LIFE_75)
          baseLifeBar.sprite.changeImage(UI_BASE_LIFE_75)
        else if(baseHp > base.getMaxHp * 0.25 && base.sprite.bm != UI_BASE_LIFE_50)
          baseLifeBar.sprite.changeImage(UI_BASE_LIFE_50)
        else if(baseHp > base.getMaxHp * 0.1 && base.sprite.bm != UI_BASE_LIFE_25)
          baseLifeBar.sprite.changeImage(UI_BASE_LIFE_25)
        else if(baseHp > 0 && !baseLifeBar.isAnimationPlaying("lowLife")) {
          baseLifeBar.playAnimation("lowLife")
        }
        else if (baseHp == 0)
          baseLifeBar.sprite.changeImage(UI_BASE_LIFE_0)
      }
      baseLifeBar.text.text = s"LIFE : $baseHp"
    })

    /** USER INFOS */
    val playerDisplaySprite: Sprite = new Sprite(UI_PLAYER_DISPLAY,
                                                  (20, WINDOW_HEIGHT - UI_PLAYER_DISPLAY.getHeight - 20))
    val playerSprite: Sprite = new Sprite(PLAYER_DEFAULT_IMAGE_BITMAP,
                                          (30 + PLAYER_DEFAULT_IMAGE_BITMAP.getWidth, WINDOW_HEIGHT - 38),
                                          _anchor = ANCHOR_BOTTOM_MIDDLE)
    val playerDisplaySupSprite: Sprite = new Sprite(UI_PLAYER_DISPLAY_SUP,
                                                    (102, WINDOW_HEIGHT - UI_PLAYER_DISPLAY_SUP.getHeight - 58))
    val playerDisplay = UiManager.createUiElement(playerDisplaySprite, LAYER_STATIC_UI_0,
                                                  offsetX = 110, offsetY = 66, color = Color.WHITE)
    val playerUI = UiManager.createUiElement(playerSprite, LAYER_STATIC_UI_1)
    val playerDisplaySup = UiManager.createUiElement(playerDisplaySupSprite, LAYER_STATIC_UI_1,
                                                     offsetX = 5, offsetY = 30, color = Color.WHITE)
    playerDisplay.addLogic(() => {
      if(EntitiesManager.player.isDefined){
        val player: Player = EntitiesManager.player.get
        playerDisplay.text.text = player.coins.toString
      }
    })

    playerUI.addLogic(() => {
      if(EntitiesManager.player.isDefined){
        val player: Player = EntitiesManager.player.get
        if(!playerUI.sprite.bm.name.contains(player.sprite.bm.name)){
          playerUI.sprite.changeImage(player.sprite.bm.copy())
          playerUI.sprite.scale = 2
        }
      }
    })

    playerDisplaySup.addLogic(() => {
      if(EntitiesManager.player.isDefined){
        val player: Player = EntitiesManager.player.get
        playerDisplaySup.text.text = s"LVL : ${player.getLvl.toString}"
      }
    })

    /** General Infos */
    val generalInfosSprite: Sprite =
      new Sprite(UI_BLANK,(WINDOW_WIDTH/2, WINDOW_HEIGHT/2), _anchor = ANCHOR_MIDDLE)
    val enterKeySprite: Sprite =
      new Sprite(UI_ENTER_KEY,(WINDOW_WIDTH/2 -50, WINDOW_HEIGHT/2 - 100), _anchor = ANCHOR_MIDDLE)
    val enterKey =
      UiManager.createUiElement(enterKeySprite, LAYER_STATIC_UI_0, color = Color.WHITE, font = pixelFontBig)
    val generalInfos =
      UiManager.createUiElement(generalInfosSprite, LAYER_STATIC_UI_0, color = Color.WHITE, font = pixelFontEnormous)

    generalInfos.addLogic(() => {
      val timer: Double = 10 - (gameTimer - prevWaveEndedTime)/1000.0
      if(timer <= 5 && !isWavePlaying && isReadyToStart) {
        generalInfos.text.offsetX = 0
        generalInfos.text.offsetY = 0
        generalInfos.text.font = pixelFontEnormous
        if (timer > 4) {
          if(generalInfos.text.text != "5") {
            generalInfos.text.text = "5"
            new BetterAudio(UI_COUNTER_AUDIO).play()
          }
        } else if (timer > 3) {
          if(generalInfos.text.text != "4") {
            generalInfos.text.text = "4"
            new BetterAudio(UI_COUNTER_AUDIO).play()
          }
        } else if (timer > 2) {
            if(generalInfos.text.text != "3") {
              generalInfos.text.text = "3"
              new BetterAudio(UI_COUNTER_AUDIO).play()
            }
        } else if (timer > 1) {
            if(generalInfos.text.text != "2") {
              generalInfos.text.text = "2"
              new BetterAudio(UI_COUNTER_AUDIO).play()
            }
        } else if (timer > 0) {
            if(generalInfos.text.text != "1") {
              generalInfos.text.text = "1"
              new BetterAudio(UI_COUNTER_AUDIO).play()
            }
        }
      }
      else if(timer > -2 && timer < 0 && isReadyToStart) {
        if(generalInfos.text.text != s"WAVE $waveCounter") {
          generalInfos.text.offsetX = -180
          generalInfos.text.text = s"WAVE $waveCounter"
        }
      }
      else if(timer < 10 && timer > 5 && isReadyToStart){
        if(generalInfos.text.text != "New Wave Coming..."){
          generalInfos.text.offsetX = - WINDOW_WIDTH /2 + 200
          generalInfos.text.offsetY = - WINDOW_HEIGHT /2 + 60
          generalInfos.text.font = pixelFontBig
          generalInfos.text.text = "New Wave Coming..."
        }
      }
      else {
        generalInfos.text.text = ""
        generalInfos.text.offsetX = 0
        generalInfos.text.offsetY = 0
      }

    })
    generalInfos.addLogic(() => {
      if(!isReadyToStart){
        if(generalInfos.text.text != "To Start The Wave"){
          enterKey.text.offsetX = -80
          enterKey.text.offsetY = 32
          enterKey.text.text = "Press"
          generalInfos.text.offsetX = -10
          generalInfos.text.offsetY = -85
          generalInfos.text.font = pixelFontBig
          generalInfos.text.text = "To Start The Wave"
        }
      }
      else
        enterKey.hide()
    })

    /** Game Over UI */
    val gameOverText: StaticUiElement = UiManager.createUiElement(generalInfosSprite, LAYER_STATIC_UI_0)
    val wavesClearedText: StaticUiElement = UiManager.createUiElement(generalInfosSprite, LAYER_STATIC_UI_1)
    val killsText: StaticUiElement = UiManager.createUiElement(generalInfosSprite, LAYER_STATIC_UI_1)
    gameOverText.addLogic(() => {
      if(isGameOver){
        if(gameOverText.text.text != "GAME OVER !"){
          gameOverText.text.text = "GAME OVER !"
          BetterAudio.playNewAudio("main theme", new BetterAudio(THEME_SONG_CHILL_MOD), LOOP_CONTINUOUSLY)
          new BetterAudio(BAKA_AUDIO).play()
          gameOverText.text.font = pixelFontEnormous
          gameOverText.text.offsetX = -300
          gameOverText.text.offsetY = -200
        }
        if(wavesClearedText.text.text != s"WAVES CLEARED: ${waveCounter-1}"){
          wavesClearedText.text.text = s"WAVES CLEARED: ${waveCounter-1}"
          wavesClearedText.text.font = pixelFontBig
          wavesClearedText.text.color = Color.WHITE
          wavesClearedText.text.offsetX = -100
          wavesClearedText.text.offsetY = -150
        }
        if(killsText.text.text != s"ENEMIES KILLED: ${EntitiesManager.enemiesKilled}"){
          killsText.text.text = s"ENEMIES KILLED: ${EntitiesManager.enemiesKilled}"
          killsText.text.font = pixelFontBig
          killsText.text.color = Color.WHITE
          killsText.text.offsetX = -100
          killsText.text.offsetY = -100
        }
      }
    })

    /** Infos Waves + Kills Display */
    val infosDisplaySprite: Sprite = new Sprite(UI_BOARD, (20,20))
    val blankSprite: Sprite = new Sprite(UI_BLANK,(20,20), _anchor = ANCHOR_MIDDLE)
    val infosDisplayBoard: StaticUiElement = UiManager.createUiElement(infosDisplaySprite, LAYER_STATIC_UI_0)
    val infosDisplayWave: StaticUiElement = UiManager.createUiElement(blankSprite, LAYER_STATIC_UI_1,
                                                                      offsetX = 20, offsetY = 40, color = Color.WHITE)
    val infosDisplayKills: StaticUiElement = UiManager.createUiElement(blankSprite, LAYER_STATIC_UI_1,
                                                                       offsetX = 20, offsetY = 70, color = Color.YELLOW)
    infosDisplayWave.addLogic(() => {
      val status: String = s"Wave : $waveCounter"
      if(infosDisplayWave.text.text != status){
        infosDisplayWave.text.text = status
      }
    })
    infosDisplayKills.addLogic(() => {
      val status: String = s"Kills : ${EntitiesManager.enemiesKilled}"
      if(infosDisplayKills.text.text != status){
        infosDisplayKills.text.text = status
      }
    })

    val buildButtonSprite: Sprite = new Sprite(UI_Q_KEY, (250, WINDOW_HEIGHT - UI_Q_KEY.getHeight - 20))
    val buildButton = UiManager.createUiElement(buildButtonSprite, LAYER_STATIC_UI_0,
                                                "Build Tower", 80, UI_Q_KEY.getHeight/2 + 16, Color.WHITE, pixelFont)


  }

  def ready(): Unit = {
    if(!isReadyToStart) {
      isReadyToStart = true
      prevWaveEndedTime = gameTimer
      println("ready : " + gameTimer)
    }
  }

  def startWave(): Unit = {
    waveCounter += 1
    isWavePlaying = true
    EntitiesManager.startWave(waveCounter)
    BetterAudio.playNewAudio("main theme",new BetterAudio(Constants.THEME_SONG_HARD), LOOP_CONTINUOUSLY)
  }

  def stopWave(): Unit = {
    isWavePlaying = false
    prevWaveEndedTime = gameTimer
    new BetterAudio(Constants.THEME_SONG_TRANS).play()
    BetterAudio.playNewAudio("main theme",new BetterAudio(Constants.THEME_SONG_CHILL), LOOP_CONTINUOUSLY)
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
    isGameOver = true
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
      if(!isPaused && !isGameOver) {
        gameTimer += System.currentTimeMillis() - prevTime

        GameManager.update()
        UiManager.updateLogics()
      }
      prevTime = System.currentTimeMillis()
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
    val player: Player = EntitiesManager.player.getOrElse(return)
    if (!pressed){
      if(player.isBuilding){
        if (mouseButton == MouseEvent.BUTTON1 && cell.state == CellStates.EMPTY) {
          if(player.build(cell)) {
            if(cell.entityPlaced.isDefined && cell.entityPlaced.get.isInstanceOf[Building]) {
              changeCursor(CURSOR_SELL)
            }
          }
        }
        else if(mouseButton == MouseEvent.BUTTON3){
          changeGameMode()
        }
      }
      else {
        if (mouseButton == MouseEvent.BUTTON1) {
          player.target = None
          player.isAttacking = false
          player.calculatePath(cell.pos._1, cell.pos._2)
        }
      }
    }
  }

  def handleEntityMouseAction(mouseButton: Int, pressed: Boolean, entity: Entity): Unit = {
    ensureInitialized()
    if(isPaused)
      return
    val player: Player = EntitiesManager.player.getOrElse(return)
    if (!pressed) {
      if(player.isBuilding) {
        if (mouseButton == MouseEvent.BUTTON1) {
          entity match {
            case building: Building => player.sell(building)
            case _ => return
          }
        }
      }
      else {
        if (mouseButton == MouseEvent.BUTTON1) {
          entity match {
            case enemy: Enemy => player.updateTarget(Some(enemy))
            case building: Building =>
              if(player.upgrade(building))
                changeCursor(CURSOR_UPGRADE)
              else
                changeCursor(CURSOR_NO_UPGRADE)

            case _ => return
          }
        }
      }
    }
  }

  def handleOnEntityEntered(entity: Entity): Unit = {
    val player = EntitiesManager.player.getOrElse(return)
    if(player.isBuilding) {
      entity match {
        case _: Enemy => changeCursor(CURSOR_NO_BUILD)
        case _: Player => changeCursor(CURSOR_NO_BUILD)
        case _: Base => changeCursor(CURSOR_NO_BUILD)
        case _: Building => changeCursor(CURSOR_SELL)
        case _ =>
      }
    }
    else {
      entity match {
        case enemy : Enemy => changeCursor(CURSOR_ATTACK)
        case building: Building =>
          if(player.canAffordUpgrade(building))
            changeCursor(CURSOR_UPGRADE)
          else
            changeCursor(CURSOR_NO_UPGRADE)

        case _ =>
      }
    }
  }

  def handleOnEntityLeft(entity: Entity): Unit = {
    val player = EntitiesManager.player.getOrElse(return)
    if(player.isBuilding) {
      entity match {
        case _ =>
          if(player.canAffordBuild())
            changeCursor(CURSOR_BUILD)
          else
            changeCursor(CURSOR_NO_BUILD)
      }
    }
    else {
      entity match {
        case _ => changeCursor(CURSOR_DEFAULT)
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
    fg.setKeyManager(new KeyAdapter() {
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
