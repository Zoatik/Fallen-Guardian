import Constants.{NUMBER_OF_LAYERS, WINDOW_HEIGHT, WINDOW_WIDTH}
import hevs.graphics.FunGraphics
import hevs.graphics.utils.GraphicsBitmap

import java.awt.Color

/**
 * Ready to render sprite class
 *
 * @param imagePath base sprite image path from src
 * @param pos       absolute pos in 2D space
 * @param scale     scale of the image
 * @param angle     rotation angle of the image
 */
class Sprite(var imagePath: String,
             var pos: (Int, Int) = (0,0),
             var scale: Double = 1,
             var angle: Double = 0
            ) {
  var bm = new GraphicsBitmap(imagePath)

  /**
   * Changes the base image of the sprite
   * @param newImagePath new image path
   */
  def changeImage(newImagePath: String): Unit = {
    this.imagePath = newImagePath
    this.bm = new GraphicsBitmap(newImagePath)
  }

  /**
   * Sets the absolute position of the sprite
   * @param newPos new absolute position
   */
  def setPosition(newPos: (Int, Int)): Unit = {
    this.pos = newPos
  }
}

/**
 * Layers object - contains all layers infos
 */
object Layers{
  var size: Int = NUMBER_OF_LAYERS
  var layerArray: Array[Layer] = Array.ofDim(size)
  for (i <- 0 until size){
    layerArray(i) = new Layer(i)
  }

  /**
   * Adds a sprite to a given layer
   * @param z layer height
   * @param sprite sprite to add
   */
  def addSprite(z: Int, sprite: Sprite): Unit = layerArray(z).addSprite(sprite)

  /**
   * creates a new layer
   */
  def addLayer(): Unit = {
    layerArray :+= new Layer(size)
    size += 1
  }

  /**
   * sets the layer from a given height
   * @param z layer height
   * @param layer new layer
   */
  def setLayer(z: Int, layer: Layer): Unit = {
    layerArray(z) = layer
  }
}

/**
 * Layer class - contains an array of sprites
 * @param z the layer height in its Layers array
 */
class Layer(var z: Int){
  var spritesArray: Array[Sprite] = Array.ofDim(0)

  /**
   * Adds a sprite to the layer
   * @param sprite sprite to add
   */
  def addSprite(sprite: Sprite): Unit = spritesArray :+= sprite
}

/**
 * Renderer object - handles the layers rendering
 */
object Renderer {
  var offsetX: Int = 0
  var offsetY: Int = 0

  /**
   * Moves the rendering offset
   * @param deltaX  X delta
   * @param deltaY  Y delta
   * @param deltaT  Time passed between two calls
   */
  def move(deltaX: Int, deltaY: Int, deltaT: Int): Unit = {
    this.offsetX += deltaX * deltaT
    this.offsetY += deltaY * deltaT
  }

  /**
   * Renders all layers in an ordered manner
   *
   * Must be called in the main Loop
   * @param fg      FunGraphics target
   * @param layers  layers to render
   */
  def render(fg: FunGraphics): Unit = {
    // Rendering (note the synchronized)
    fg.frontBuffer.synchronized {
      fg.clear(Color.white)
      for (layer <- Layers.layerArray) {
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
     */
    fg.syncGameLogic(60)
  }
}