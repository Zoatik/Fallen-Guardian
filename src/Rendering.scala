import Constants.{WINDOW_HEIGHT, WINDOW_WIDTH}
import hevs.graphics.FunGraphics
import hevs.graphics.utils.GraphicsBitmap

import java.awt.Color

class Sprite(var imagePath: String,
             var pos: (Int, Int),
             var scale: Double = 1,
             var angle: Double = 0,
             var layerZ: Int = 0
            ) {
  var bm = new GraphicsBitmap(imagePath)

  def changeImage(newImagePath: String): Unit = {
    this.imagePath = newImagePath
    this.bm = new GraphicsBitmap(newImagePath)
  }
}


class Layers(var size: Int) {
  var layerArray: Array[Layer] = Array.ofDim(size)
  for (i <- 0 until size){
    layerArray(i) = new Layer(i)
  }

  def addLayer(): Unit = {
    layerArray :+= new Layer(size)
    size += 1
  }

  def setLayer(z: Int, layer: Layer): Unit = {
    layerArray(z) = layer
  }
}

class Layer(var z: Int){
  var spritesArray: Array[Sprite] = Array.ofDim(0)
  def addSprite(sprite: Sprite): Unit = {
    spritesArray :+= sprite
  }
}

object Renderer {
  var offsetX: Int = 0
  var offsetY: Int = 0

  // Moves the cells by a delta
  def move(deltaX: Int, deltaY: Int, deltaT: Int): Unit = {
    this.offsetX += deltaX * deltaT
    this.offsetY += deltaY * deltaT
  }

  // Main rendering function
  def render(fg: FunGraphics, layers: Layers): Unit = {
    // Rendering (note the synchronized)
    fg.frontBuffer.synchronized {
      fg.clear(Color.white)
      for (layer <- layers.layerArray) {
        for (sprite <- layer.spritesArray) {
          val x = sprite.pos._1 + sprite.bm.getWidth/2 + offsetX
          val y = sprite.pos._2 + sprite.bm.getHeight/2 + offsetY
          val angle = sprite.angle
          val scale = sprite.scale
          val bm = sprite.bm
          fg.drawTransformedPicture(x, y, angle, scale, bm)
        }
      }
    }

    /**
     * Pause for constant frame rate
     * */
    fg.syncGameLogic(60)
  }
}