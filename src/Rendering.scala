import Constants.{ANCHOR_BOTTOM_MIDDLE, ANCHOR_MIDDLE, ANCHOR_TOP_LEFT, LAYER_ENTITIES, LAYER_UI_MOBILE, NUMBER_OF_LAYERS, NUMBER_OF_STATIC_UI_LAYERS, WINDOW_HEIGHT, WINDOW_WIDTH, pixelFont, pixelSizedFont}
import UI_Manager.StaticUiElement
import hevs.graphics.FunGraphics

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
             var pos: (Int, Int) ,
             var scale: Double ,
             var angle: Double,
             var anchor: Int,
             _bm: BetterGraphicsBitmap
            ) {
  var bm: BetterGraphicsBitmap = if(imagePath != "") new BetterGraphicsBitmap(imagePath) else _bm
  private var brightness: Double = 1.0


  def this(bitmap: BetterGraphicsBitmap,
           _pos: (Int, Int) = (0,0),
           _scale: Double = 1,
           _angle: Double = 0,
           _anchor: Int = ANCHOR_TOP_LEFT) = this(imagePath = "",
                                                  pos = _pos,
                                                  scale = _scale,
                                                  angle = _angle,
                                                  anchor = _anchor,
                                                  _bm = bitmap)


  def changeImage(newBitmap: BetterGraphicsBitmap): Unit = {
    this.bm = newBitmap
    brighten(brightness)
  }

  def brighten(factor: Double = 1.5): Unit = {
    if (factor == 1.0)
      return
    brightness = factor
    bm = bm.copy()
    val w = bm.getBufferedImage.getWidth()
    val h = bm.getBufferedImage.getHeight()
    for(x <- 0 until w; y <- 0 until h){
      val rgb = bm.getBufferedImage.getRGB(x,y)
      val alpha = (rgb >> 24) & 0xFF
      val r = (rgb >> 16) & 0xFF
      val g = (rgb >> 8) & 0xFF
      val b = rgb & 0xFF

      val newR = Math.min((r * factor).toInt, 255)
      val newG = Math.min((g * factor).toInt, 255)
      val newB = Math.min((b * factor).toInt, 255)


      val newRgb = (alpha << 24) | (newR << 16) | (newG << 8) | newB

      bm.getBufferedImage.setRGB(x, y, newRgb)
    }


  }

  def restoreImage(original: BetterGraphicsBitmap): Unit = {
    brightness = 1
    bm = original
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
  var layersSize: Int = NUMBER_OF_LAYERS
  var staticUiLayersSize: Int = NUMBER_OF_STATIC_UI_LAYERS
  var layerArray: Array[Layer] = Array.ofDim(layersSize)
  var staticUiArray: Array[StaticUiLayer] = Array.ofDim(staticUiLayersSize)
  for (i <- 0 until layersSize){
    layerArray(i) = new Layer(i)
  }
  for(i <- 0 until staticUiLayersSize){
    staticUiArray(i) = new StaticUiLayer(i)
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

  def addStaticUiElement(z: Int, el: StaticUiElement): Unit = staticUiArray(z).addStaticUiElement(el)

  def removeStaticUiElement(el: StaticUiElement): Unit = {
    staticUiArray.find(layer => layer.staticUiList.contains(el)).getOrElse(return).staticUiList -= el
  }
  /**
   * creates a new layer
   */
  def addLayer(): Unit = {
    layerArray :+= new Layer(layersSize)
    layersSize += 1
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

class StaticUiLayer(var z: Int){
  var staticUiList:  mutable.ListBuffer[StaticUiElement] = mutable.ListBuffer()

  def addStaticUiElement(staticUiElement: StaticUiElement): Unit = staticUiList += staticUiElement
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

  def setCenterPosition(pos: (Int, Int)): Unit = {
    this.offsetX = -pos._1 + WINDOW_WIDTH / 2
    this.offsetY = -pos._2 + WINDOW_HEIGHT / 2
  }

  /**
   * Renders all layers in an ordered manner
   *
   * Must be called in the main Loop
   * @param fg      FunGraphics target
   * @param layers  layers to render
   */
  def render(fg: BetterFunGraphics): Unit = {
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

      for(layer <- Layers.staticUiArray) {
        for (staticUiElement <- layer.staticUiList) {
          val topLeftPos = staticUiElement.sprite.getTopLeftPos()
          val x = topLeftPos._1 + staticUiElement.sprite.bm.getWidth / 2
          val y = topLeftPos._2 + staticUiElement.sprite.bm.getHeight / 2
          val angle = staticUiElement.sprite.angle
          val scale = staticUiElement.sprite.scale
          val bm = staticUiElement.sprite.bm
          val textX = topLeftPos._1 + staticUiElement.text.offsetX
          val textY = topLeftPos._2 + staticUiElement.text.offsetY
          val text: String = staticUiElement.text.text
          fg.drawTransformedPicture(x, y, angle, scale, bm)
          fg.drawString(textX, textY, text, pixelSizedFont, Color.BLACK)
          //fg.drawString(0, 30, "HELLO WORLD IN PIXEL FONT", pixelSizedFont, Color.BLACK)
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