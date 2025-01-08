object Constants {
  val WINDOW_WIDTH: Int = 1400//1600
  val WINDOW_HEIGHT: Int = 1000//1200
  val GRID_SIZE: Int = 60
  val CELL_SIZE: Int = 32
  val MAX_INPUT_BUFFER_SIZE: Int = 10
  val COLLISION_TIME_DELAY: Int = 10
  val NUMBER_OF_LAYERS: Int = 3

  //* ------------------------* */
    // Player spawn constants
  val defaultPos: (Int, Int) = (1*CELL_SIZE, 1*CELL_SIZE)
  val defaultHp: Int = 10
  val defaultArmor: Int = 5

  val defaultSpritePath: String = "/res/Characters/Soldier/idle/soldierIdle0.png"
  val defaultScale: Double = 1
  val defaultAngle: Int = 0
  val defaultLayerZ: Int = 1
  val defaultSprite: Sprite = new Sprite(defaultSpritePath, defaultPos, defaultScale, defaultAngle)


  val defaultBox: Box = new Box(defaultPos._1, defaultPos._2, 10, 10)
  val defaultBoxCollision2D: CollisionBox2D = new CollisionBox2D("Player Collider", defaultBox)
}

