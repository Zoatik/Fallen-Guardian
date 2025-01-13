import java.awt.event.KeyEvent

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

  //* ------------------------* */
    // Player spawn constants
  val PLAYER_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val PLAYER_DEFAULT_HP: Int = 10
  val PLAYER_DEFAULT_ARMOR: Int = 5

  val PLAYER_DEFAULT_IMAGE_PATH: String = "/res/Characters/Soldier/idle/soldierIdle_0.png"
  val PLAYER_DEFAULT_VELOCITY: Double = 4.0
  val PLAYER_DEFAULT_DAMAGE: Int = 8
  val PLAYER_DEFAULT_COINS: Int = 100

  //* ------------------------* */
  // Tower constants
  val ENEMY_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val ENEMY_DEFAULT_HP: Int = 10
  val ENEMY_DEFAULT_ARMOR: Int = 1

  val ENEMY_DEFAULT_IMAGE_PATH: String = "/res/Characters/Enemy/Orc/idle/orcIdle_0.png"
  val ENEMY_DEFAULT_VELOCITY: Double = 1.8
  val ENEMY_DEFAULT_DAMAGE: Int = 2

  //* ------------------------* */
    // Tower constants
  val TOWER_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val TOWER_DEFAULT_HP: Int = 100
  val TOWER_DEFAULT_ARMOR: Int = 50

  val TOWER_DEFAULT_IMAGE_PATH: String = "/res/Tower/IdleAnimation/tower_00.png"
  val TOWER_DEFAULT_DAMAGE: Int = 4
  val TOWER_DEFAULT_RANGE: Int = 128
  val TOWER_DEFAULT_ATTACK_SPEED: Double = 4

  //* ------------------------* */
  // Tower bullet constants
  val BULLET_VELOCITY: Int = 5
  val BULLET_DEFAULT_IMAGE_PATH: String = "/res/Characters/Enemy/Orc/idle/orcIdle_0.png"//"/res/Tower/bullet/bullet_0.png"

  //* ------------------------* */
  // Build system constants
  val BUILD_TOWER: Int = 0
  val BUILD_BARRICADE: Int = 1
  val BUILD_TOWER_PRICE: Int = 50
  val BUILD_BARRICADE_PRICE: Int = 10

  //* ------------------------* */
  // Waves constants -> time in milliseconds
  val WAVE_TIME: Int = 180_000
  val WAVE_PAUSE_TIME: Int = 10_000
}

