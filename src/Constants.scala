import java.awt.event.KeyEvent

object Constants {
  val WINDOW_WIDTH: Int = 1400//1600
  val WINDOW_HEIGHT: Int = 1000//1200
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


  //* ------------------------* */
    // Player spawn constants
  val PLAYER_DEFAULT_POS: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val PLAYER_DEFAULT_HP: Int = 10
  val PLAYER_DEFAULT_ARMOR: Int = 5

  val PLAYER_DEFAULT_IMAGE_PATH: String = "/res/Characters/Soldier/idle/soldierIdle0.png"
  val PLAYER_DEFAULT_VELOCITY: Int = 4
  val PLAYER_DEFAULT_DAMAGE: Int = 3
  val PLAYER_DEFAULT_COINS: Int = 2
}

