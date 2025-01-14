
import java.awt.{Cursor, Point, Toolkit}
import java.awt.event.KeyEvent
import javax.imageio.ImageIO
import java.io.File

object Constants {
  val WINDOW_WIDTH: Int = 1400//1920//1400//1600
  val WINDOW_HEIGHT: Int = 1000//1080//1000//1200
  val GRID_SIZE: Int = 60
  val CELL_SIZE: Int = 32
  val MAX_INPUT_BUFFER_SIZE: Int = 10
  val COLLISION_TIME_DELAY: Int = 10


  //*-------------------------*//
    // Camera constants
  val CAMERA_UP = KeyEvent.VK_W
  val CAMERA_DOWN = KeyEvent.VK_S
  val CAMERA_LEFT = KeyEvent.VK_A
  val CAMERA_RIGHT = KeyEvent.VK_D

  //*-------------------------*//
    // Layers Constants
  val NUMBER_OF_LAYERS: Int = 4
  val LAYER_GROUND = 0
  val LAYER_ENTITIES = 1
  val LAYER_PLAYER = 2
  val LAYER_UI = 3

  //*-------------------------*//
    // Sprite Constants
  val ANCHOR_TOP_LEFT = 0
  val ANCHOR_MIDDLE = 1
  val ANCHOR_BOTTOM_MIDDLE = 2


  //* ------------------------* */
    // UI Constants
  //val COLLISION_FRAME_MAGNIFICATION = 15
  private val cursorDefaultImage = ImageIO.read(getClass.getResource("res/Cursors/defaultCursor.png"))
  private val cursorBuildImage = ImageIO.read(getClass.getResource("res/Cursors/buildCursor.png"))
  private val cursorNoBuildImage = ImageIO.read(getClass.getResource("res/Cursors/noBuildCursor.png"))
  private val cursorUpgradeImage = ImageIO.read(getClass.getResource("res/Cursors/upgradeCursor.png"))
  private val cursorNoUpgradeImage = ImageIO.read(getClass.getResource("res/Cursors/noUpgradeCursor.png"))
  private val cursorSellImage = ImageIO.read(getClass.getResource("res/Cursors/sellCursor.png"))
  private val cursorNoSellImage = ImageIO.read(getClass.getResource("res/Cursors/noSellCursor.png"))
  private val cursorAttackImage = ImageIO.read(getClass.getResource("res/Cursors/attackCursor.png"))


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


  val PLACE_HOLDER_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/ground/cellEmpty.png")

  //* ------------------------* */
    // Player spawn constants
  val PLAYER_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val PLAYER_DEFAULT_HP: Int = 50
  val PLAYER_DEFAULT_ARMOR: Int = 5

  val PLAYER_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Characters/Soldier/idle/soldierIdle_0.png")
  val PLAYER_DEFAULT_VELOCITY: Double = 4.0
  val PLAYER_DEFAULT_DAMAGE: Int = 8
  val PLAYER_DEFAULT_COINS: Int = 1000000

  //* ------------------------* */
    // Tower constants
  val ENEMY_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val ENEMY_DEFAULT_HP: Int = 5
  val ENEMY_DEFAULT_ARMOR: Int = 1

  val ENEMY_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_0.png")
  val ENEMY_DEFAULT_VELOCITY: Double = 1.8
  val ENEMY_DEFAULT_DAMAGE: Int = 1

  //* ------------------------* */
    // Tower constants
  val TOWER_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val TOWER_DEFAULT_HP: Int = 100
  val TOWER_DEFAULT_ARMOR: Int = 50

  val TOWER_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Tower/IdleAnimation/tower_00.png")
  val TOWER_DEFAULT_DAMAGE: Int = 4
  val TOWER_DEFAULT_RANGE: Int = 128
  val TOWER_DEFAULT_ATTACK_SPEED: Double = 3000

  //* ------------------------* */
    // Tower bullet constants
  val BULLET_VELOCITY: Int = 5
  val BULLET_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_0.png")//"/res/Tower/bullet/bullet_0.png"

  //* ------------------------* */
    // Base constants
  val BASE_DEFAULT_POS: (Int, Int) = (29, 29)
  val BASE_DEFAULT_HP: Int = 100
  val BASE_DEFAULT_ARMOR: Int = 1
  val BASE_DEFAULT_IMAGE_BITMAP: BetterGraphicsBitmap = new BetterGraphicsBitmap("/res/Base/base_0.png")//"/res/Tower/bullet/bullet_0.png"

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
  val THEME_SONG: Audio = new Audio("/res/Audio/theme.wav")

  val PLAYER_MOVE_AUDIO: Audio = new Audio(s"/res/Audio/PlayerAudio/WalkAudio/Walk_-${(math.random() * (14-1) + 2).toInt}.wav")
  val PLAYER_HIT_AUDIO: Audio = new Audio("/res/Audio/PlayerAudio/player_hit.wav")
  val PLAYER_DEATH_AUDIO: Audio = new Audio("/res/Audio/PlayerAudio/player_death.wav")
  val PLAYER_ATTACK_AUDIO: Audio = new Audio("/res/Audio/PlayerAudio/AttackAudio/Sword_slash2.wav")
  val PLAYER_KILL_ATTACK_AUDIO: Audio = new Audio("/res/Audio/PlayerAudio/AttackAudio/final_sword_slash.wav")

  val GOBLIN_ATTACK_AUDIO: Audio = new Audio("/res/Audio/GoblinAudio/goblin_attack.wav")
  val GOBLIN_HURT_AUDIO: Audio = new Audio("/res/Audio/GoblinAudio/goblin_hurt.wav")
  val GOBLIN_DEATH_AUDIO: Audio = new Audio("/res/Audio/GoblinAudio/goblin_death.wav")
}

