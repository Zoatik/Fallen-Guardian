package scripts.data


import scripts.tools.BetterGraphicsBitmap

import java.awt.event.KeyEvent
import java.awt._
import javax.imageio.ImageIO

object Constants {
  var WINDOW_WIDTH: Int = 1400//1920//1400//1600
  var WINDOW_HEIGHT: Int = 1000//1080//1000//1200
  val GRID_SIZE: Int = 60
  val CELL_SIZE: Int = 32
  val MAX_INPUT_BUFFER_SIZE: Int = 10
  val COLLISION_TIME_DELAY: Int = 10

  //*-------------------------*//
    // Fonts constants
  private val fontStream = getClass.getResourceAsStream("/res/Fonts/MinimalPixel v2.ttf")
  if (fontStream == null) throw new IllegalArgumentException("Font not found in resources!")
  val pixelFont: Font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(18f)
  val pixelFontSmall: Font = pixelFont.deriveFont(12f)
  val pixelFontBig: Font = pixelFont.deriveFont(24f)
  val pixelFontEnormous: Font = pixelFont.deriveFont(100f)
  fontStream.close()
  GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(pixelFont)
  GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(pixelFontSmall)
  GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(pixelFontBig)
  GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(pixelFontEnormous)

  //*-------------------------*//
    // Camera constants
  val CAMERA_UP: Int = KeyEvent.VK_W
  val CAMERA_DOWN: Int = KeyEvent.VK_S
  val CAMERA_LEFT: Int = KeyEvent.VK_A
  val CAMERA_RIGHT: Int = KeyEvent.VK_D

  //*-------------------------*//
    // Layers Constants
  val NUMBER_OF_LAYERS: Int = 4
  val NUMBER_OF_STATIC_UI_LAYERS: Int = 3
  val LAYER_GROUND = 0
  val LAYER_ENTITIES = 1
  val LAYER_PLAYER = 2
  val LAYER_UI_MOBILE = 3
  val LAYER_STATIC_UI_0 = 0
  val LAYER_STATIC_UI_1 = 1
  val LAYER_STATIC_UI_2 = 2

  //*-------------------------*//
    // Collision Layers Constants
  val NUMBER_OF_COLLISION_LAYERS: Int = 6
  val COLLISION_LAYER_GROUND = 0
  val COLLISION_LAYER_ENTITIES = 1
  val COLLISION_LAYER_BUILDING = 2
  val COLLISION_LAYER_ENEMIES = 3
  val COLLISION_LAYER_PLAYER = 4
  val COLLISION_LAYER_UI = 5

  //*-------------------------*//
    // Sprite Constants
  val ANCHOR_TOP_LEFT = 0
  val ANCHOR_MIDDLE = 1
  val ANCHOR_BOTTOM_MIDDLE = 2


  //* ------------------------* */
    // UI Constants
  private val cursorDefaultImage = ImageIO.read(getClass.getResource("/res/Cursors/defaultCursor.png"))
  private val cursorBuildImage = ImageIO.read(getClass.getResource("/res/Cursors/buildCursor.png"))
  private val cursorNoBuildImage = ImageIO.read(getClass.getResource("/res/Cursors/noBuildCursor.png"))
  private val cursorUpgradeImage = ImageIO.read(getClass.getResource("/res/Cursors/upgradeCursor.png"))
  private val cursorNoUpgradeImage = ImageIO.read(getClass.getResource("/res/Cursors/noUpgradeCursor.png"))
  private val cursorSellImage = ImageIO.read(getClass.getResource("/res/Cursors/sellCursor.png"))
  private val cursorNoSellImage = ImageIO.read(getClass.getResource("/res/Cursors/noSellCursor.png"))
  private val cursorAttackImage = ImageIO.read(getClass.getResource("/res/Cursors/attackCursor.png"))

  private val hotspotTopLeft = new Point(0,0)
  private val hotspotCenter = new Point(16,16)
  val CURSOR_DEFAULT: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(cursorDefaultImage, hotspotTopLeft, "Default Cursor")
  val CURSOR_BUILD: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(cursorBuildImage, hotspotTopLeft, "build Cursor")
  val CURSOR_NO_BUILD: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(cursorNoBuildImage, hotspotTopLeft, "no build Cursor")
  val CURSOR_UPGRADE: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(cursorUpgradeImage, hotspotTopLeft, "upgrade Cursor")
  val CURSOR_NO_UPGRADE: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(cursorNoUpgradeImage, hotspotTopLeft, "no upgrade Cursor")
  val CURSOR_SELL: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(cursorSellImage, hotspotTopLeft, "sell Cursor")
  val CURSOR_NO_SELL: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(cursorNoSellImage, hotspotTopLeft, "no sell Cursor")
  val CURSOR_ATTACK: Cursor = Toolkit.getDefaultToolkit.createCustomCursor(cursorAttackImage, hotspotCenter, "attack Cursor")

  //* ------------------------* */
    // Cell images constants
  val BITMAP_CELL_GRASS_0: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/ground/TX_grass_0.png")
  val BITMAP_CELL_GRASS_1: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/ground/TX_grass_1.png")
  val BITMAP_CELL_STONE_0: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/ground/TX_stone_0.png")
  val BITMAP_CELL_FLOWER_0: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/ground/TX_flower_0.png")
  val BITMAP_CELL_EMPTY: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/ground/cellEmpty.png")

  //* ------------------------* */
    // HP bar UI constants
  val PLAYER_HP_BAR_1: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/PlayerHealth/health1.png")
  val PLAYER_HP_BAR_2: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/PlayerHealth/health2.png")
  val PLAYER_HP_BAR_3: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/PlayerHealth/health3.png")
  val PLAYER_HP_BAR_4: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/PlayerHealth/health4.png")
  val PLAYER_HP_BAR_5: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/PlayerHealth/health5.png")

  val ENEMY_HP_BAR_1: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/EnemyHealth/enemyHealth1.png")
  val ENEMY_HP_BAR_2: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/EnemyHealth/enemyHealth2.png")
  val ENEMY_HP_BAR_3: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/EnemyHealth/enemyHealth3.png")
  val ENEMY_HP_BAR_4: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/EnemyHealth/enemyHealth4.png")
  val ENEMY_HP_BAR_5: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/EnemyHealth/enemyHealth5.png")

  val TOWER_HP_BAR_1: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/TowerHealth/towerHealth1.png")
  val TOWER_HP_BAR_2: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/TowerHealth/towerHealth2.png")
  val TOWER_HP_BAR_3: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/TowerHealth/towerHealth3.png")
  val TOWER_HP_BAR_4: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/TowerHealth/towerHealth4.png")
  val TOWER_HP_BAR_5: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/TowerHealth/towerHealth5.png")

  val UI_BLANK: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/blank.png")
  val UI_PLAYER_DISPLAY: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/playerStats.png")
  val UI_PLAYER_DISPLAY_SUP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/playerStatsLvl.png")
  val UI_BASE_LIFE_0: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/life_0.png")
  val UI_BASE_LIFE_1: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/life1_0.png")
  val UI_BASE_LIFE_1_HIGHLIGHTED: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/life1_1.png")
  val UI_BASE_LIFE_25: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/life_25.png")
  val UI_BASE_LIFE_50: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/life_50.png")
  val UI_BASE_LIFE_75: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/life_75.png")
  val UI_BASE_LIFE_100: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/life_100.png")
  val UI_BOARD: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/board.png")

  val UI_ENTER_KEY: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/Enter-Key.png")
  val UI_ESC_KEY: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/Esc-Key.png")
  val UI_Q_KEY: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/UI/Static/Q-Key.png")

  //* ------------------------* */
    // Player constants
  val PLAYER_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val PLAYER_DEFAULT_HP: Int = 30
  val PLAYER_DEFAULT_ARMOR: Int = 1

  val PLAYER_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Characters/Soldier/idle/soldierIdle_0.png")
  val PLAYER_DEFAULT_VELOCITY: Double = 4.0
  val PLAYER_DEFAULT_DAMAGE: Int = 2
  val PLAYER_DEFAULT_COINS: Int = 100

  //* ------------------------* */
    // Enemy constants
  val ENEMY_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val ENEMY_DEFAULT_HP: Int = 5
  val ENEMY_DEFAULT_ARMOR: Int = 1

  val ENEMY_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_0.png")
  val ENEMY_DEFAULT_VELOCITY: Double = 1.8
  val ENEMY_DEFAULT_DAMAGE: Int = 2

  //* ------------------------* */
    // Tower constants
  val TOWER_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val TOWER_DEFAULT_HP: Int = 25
  val TOWER_DEFAULT_ARMOR: Int = 5

  val TOWER_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Tower/IdleAnimation/tower_00.png")
  val TOWER_DEFAULT_DAMAGE: Int = 1
  val TOWER_DEFAULT_RANGE: Int = 128
  val TOWER_DEFAULT_ATTACK_SPEED: Double = 4000

  //* ------------------------* */
    // Tower bullet constants
  val BULLET_VELOCITY: Int = 5
  val BULLET_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Tower/Bullet/bullet_0.png")

  //* ------------------------* */
    // Base constants
  val BASE_DEFAULT_POS: (Int, Int) = (29, 29)
  val BASE_DEFAULT_HP: Int = 100
  val BASE_DEFAULT_ARMOR: Int = 0
  val BASE_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Base/base_0.png")

  //* ------------------------* */
    // Build system constants
  val BUILD_TOWER: Int = 0
  val BUILD_BARRICADE: Int = 1
  val BUILD_TOWER_PRICE: Int = 50
  val BUILD_BARRICADE_PRICE: Int = 10

  //* ------------------------* */
    // Waves constants -> time in milliseconds
  val WAVE_TIME: Int = 60_000
  val WAVE_PAUSE_TIME: Int = 10_000

  //* ------------------------* */
    // Audio constants
  val THEME_SONG_CHILL: String = "/res/Audio/MainTheme/MainThemeChill.wav"
  val THEME_SONG_HARD: String = "/res/Audio/MainTheme/MainThemeHard.wav"
  val THEME_SONG_TRANS: String = "/res/Audio/MainTheme/MainThemeTrans.wav"
  val THEME_SONG_CHILL_MOD: String = "/res/Audio/MainTheme/MainThemeChillModified.wav"

  val PLAYER_MOVE_AUDIO: String  = "/res/Audio/PlayerAudio/WalkAudio/Walk_-1.wav"
  val PLAYER_HIT_AUDIO: String = "/res/Audio/PlayerAudio/player_hit.wav"
  val PLAYER_DEATH_AUDIO: String = "/res/Audio/PlayerAudio/player_death.wav"
  val PLAYER_ATTACK_AUDIO: String = "/res/Audio/PlayerAudio/AttackAudio/Sword_slash2.wav"
  val PLAYER_KILL_ATTACK_AUDIO: String = "/res/Audio/PlayerAudio/AttackAudio/final_sword_slash.wav"

  val GOBLIN_ATTACK_AUDIO: String = "/res/Audio/GoblinAudio/goblin_attack.wav"
  val GOBLIN_HURT_AUDIO: String = "/res/Audio/GoblinAudio/goblin_hurt.wav"
  val GOBLIN_DEATH_AUDIO: String = "/res/Audio/GoblinAudio/goblin_death.wav"

  val PLAYER_XP_GAIN_AUDIO: String = "/res/Audio/Interactions/xpAudio.wav"
  val PLAYER_LEVEL_UP_AUDIO: String = "/res/Audio/Interactions/playerLevelUp.wav"
  val TOWER_PLACEMENT_AUDIO: String = "/res/Audio/Interactions/towerPlacement.wav"
  val TOWER_LEVEL_UP_AUDIO: String = "/res/Audio/Interactions/towerLevelUp.wav"
  val SELL_AUDIO: String = "/res/Audio/Interactions/sellAudio.wav"

  val UI_COUNTER_AUDIO: String = "/res/Audio/OtherSoundEffects/counterImpact.wav"
  val BAKA_AUDIO: String = "/res/Audio/OtherSoundEffects/baka.wav"
}

