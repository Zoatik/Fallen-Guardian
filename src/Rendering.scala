import Constants.{ANCHOR_BOTTOM_MIDDLE, ANCHOR_MIDDLE, ANCHOR_TOP_LEFT, LAYER_ENTITIES, NUMBER_OF_LAYERS, WINDOW_HEIGHT, WINDOW_WIDTH}
import hevs.graphics.FunGraphics
import hevs.graphics.utils.GraphicsBitmap

import java.awt.Color
import scala.collection.mutable

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
             var angle: Double = 0,
             var anchor: Int = ANCHOR_TOP_LEFT
            ) {
  var bm = new GraphicsBitmap(imagePath)
  private var brightness: Double = 1.0

  /**
   * Changes the base image of the sprite
   * @param newImagePath new image path
   */
  def changeImage(newImagePath: String): Unit = {
    this.imagePath = newImagePath
    this.bm = new GraphicsBitmap(newImagePath)
    brighten(brightness)
  }

  def brighten(factor: Double = 1.5): Unit = {
    brightness = factor
    val w = bm.getBufferedImage.getWidth()
    val h = bm.getBufferedImage.getHeight()
    for(x <- 0 until w; y <- 0 until h){
      val rgb = bm.getBufferedImage.getRGB(x,y)
      val alpha = (rgb >> 24) & 0xFF
      val r = (rgb >> 16) & 0xFF
      val g = (rgb >> 8) & 0xFF
      val b = rgb & 0xFF

      // Augmenter la luminosité en limitant à 255
      val newR = Math.min((r * factor).toInt, 255)
      val newG = Math.min((g * factor).toInt, 255)
      val newB = Math.min((b * factor).toInt, 255)


      // Recomposer la nouvelle valeur RGB
      val newRgb = (alpha << 24) | (newR << 16) | (newG << 8) | newB

      // Mettre à jour le pixel
      bm.getBufferedImage.setRGB(x, y, newRgb)
    }


  }

  def restoreImage(): Unit = {
    bm = new GraphicsBitmap(imagePath)
    brightness = 1

  }

  /**
   * Sets the absolute position of the sprite
   * @param newPos new absolute position
   */
  def setPosition(newPos: (Int, Int)): Unit = {
    this.pos = newPos
  }


  def setTopLeftPosition(newTopLeftPos: (Int, Int)): Unit = {
    if(this.anchor == ANCHOR_TOP_LEFT)
      this.pos = newTopLeftPos
    else if(this.anchor == ANCHOR_MIDDLE)
      this.pos = (newTopLeftPos._1 + bm.getWidth/2, newTopLeftPos._2 + bm.getHeight/2)
    else // ANCHOR_BOTTOM_MIDDLE
      this.pos = (newTopLeftPos._1 + bm.getWidth/2, newTopLeftPos._2 + bm.getHeight)
  }

  def getTopLeftPos(): (Int, Int) = {
    if(this.anchor == ANCHOR_TOP_LEFT) {
      return this.pos
    }
    if(this.anchor == ANCHOR_MIDDLE)
      return (this.pos._1 - bm.getWidth/2, this.pos._2 - bm.getHeight/2)
    // ANCHOR_BOTTOM_MIDDLE
    (this.pos._1 - bm.getWidth/2, this.pos._2 - bm.getHeight)
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

  def removeSprite(sprite: Sprite): Unit = {
    layerArray.find(layer => layer.spritesList.contains(sprite)).getOrElse(return).spritesList -= sprite
  }

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
  var spritesList: mutable.ListBuffer[Sprite] = mutable.ListBuffer()

  /**
   * Adds a sprite to the layer
   * @param sprite sprite to add
   */
  def addSprite(sprite: Sprite): Unit = spritesList += sprite
}

/**
 * Renderer object - handles the layers rendering
 */
object Renderer {
  var offsetX: Int = 0
  var offsetY: Int = 0

  private var prevTime: Long = System.currentTimeMillis()
  var deltaT: Long = 0


  def destroy(sprite: Sprite): Unit = {
    Layers.removeSprite(sprite)
  }

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
        for (sprite <- layer.spritesList) {
          val topLeftPos = sprite.getTopLeftPos()
          val x = topLeftPos._1 + sprite.bm.getWidth/2 + offsetX
          val y = topLeftPos._2 + sprite.bm.getHeight/2 + offsetY
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
    val currTime = System.currentTimeMillis()
    deltaT = currTime - prevTime
    prevTime = currTime
    fg.syncGameLogic(60)
  }
}