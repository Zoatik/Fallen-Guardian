import java.awt.event.{KeyAdapter, KeyEvent}
import java.awt.Color
import hevs.graphics.FunGraphics
import Constants._
import Renderer._

object ImageAnimation extends App {

  val fg = new FunGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, "Lost In Hell", false)
  val im = new InputManager()
  val grid: Grid = new Grid((GRID_SIZE,GRID_SIZE), 32)
  var layers: Layers = new Layers(3)
  for( el <- grid.cells){
    for(cell <- el){
      layers.layerArray(0).addSprite(cell.sprite)
    }
  }



  var counter: Int = 0
  var m: Int = 0
  var n: Int = 0

  private def cellTest(): Unit = {

    grid.cells(m)(n).sprite.changeImage("/res/cellTest2.png")
    counter += 1
    m = counter % (GRID_SIZE)
    n = counter / (GRID_SIZE)

  }

  im.bind(KeyEvent.VK_C, cellTest())

  fg.setKeyManager(new KeyAdapter() { // Will be called when a key has been pressed
    override def keyPressed(e: KeyEvent): Unit = {
      im.handleKeyPressed(e.getKeyCode)
    }

    /*override def keyReleased(e: KeyEvent): Unit = {
      im.handleKeyReleased()
    }*/
  })


  /** Main Loop **/
  while (true) {

    render(fg, layers)
  }
}



// Exemple d'utilisation pour collision
object GridCollisionApp extends App {
  val box1 = new Box2DCollision("Box1", Box(0, 0, 10, 10))
  val box2 = new Box2DCollision("Box2", Box(5, 5, 10, 10))

  Box2DCollisionManager.register(box1)
  Box2DCollisionManager.register(box2)

  box1.onCollision { other =>
    println(s"${box1.id} collided with ${other.id}")
  }

  box2.onCollision { other =>
    println(s"${box2.id} collided with ${other.id}")
  }

  // Déplace la première boîte pour provoquer une collision
  box1.setPosition(1,1)
}






