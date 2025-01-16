package scripts.tools

import hevs.graphics.FunGraphics

import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.{GraphicsConfiguration, GraphicsEnvironment}
import javax.imageio.ImageIO

class BetterFunGraphics(_width: Int, _height: Int, _title: String, _highQuality: Boolean)
  extends FunGraphics(width = _width, height = _height, title = _title, high_quality = _highQuality){
  def drawTransformedPicture(posX: Int, posY: Int, angle: Double, scale: Double, bitmap: BetterGraphicsBitmap): Unit = {
    val t = new AffineTransform
    t.rotate(angle, posX, posY)
    t.translate(posX - bitmap.getWidth / 2 * scale, posY - bitmap.getHeight / 2 * scale)
    t.scale(scale, scale)
    g2d.drawImage(bitmap.mBitmap, t, null)
  }
}

/**
 * GraphicsBitmap contains the methods required to create a [[BufferedImage]] from a
 * [[String]] if the file exists
 *
 * 1.3 : Added acceleration for images using graphics card
 *
 * @version 1.3, April 2011
 * @author <a href='mailto:pandre.mudry&#64;hevs.ch'> Pierre-Andre Mudry</a>
 */
class BetterGraphicsBitmap(val name: String, var mBitmap: BufferedImage = null) {
  final private var WIDTH = 0
  final private var HEIGHT = 0
  val gc: GraphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDefaultConfiguration
  if(mBitmap == null) {
    try {
      val v = classOf[BetterGraphicsBitmap].getResource(name)
      mBitmap = ImageIO.read(v)
    } catch {
      case e: Exception =>
        System.out.println("Could not find image " + name + ", exiting !")
        e.printStackTrace()
        System.exit(-1)
    } finally if (mBitmap != null) {
      WIDTH = mBitmap.getWidth
      HEIGHT = mBitmap.getHeight
    }
    else {
      WIDTH = 0
      HEIGHT = 0
    }
  }
  else {
    WIDTH = mBitmap.getWidth
    HEIGHT = mBitmap.getHeight
  }

  /**
   * @return width of the image
   */
  def getWidth: Int = WIDTH

  /**
   * @return height of the image
   */
  def getHeight: Int = HEIGHT

  /**
   * @return the [[BufferedImage]] corresponding to the
   *         [[BetterGraphicsBitmap]]
   */
  def getBufferedImage: BufferedImage = mBitmap

  def copy(): BetterGraphicsBitmap = {
    if (mBitmap == null) {
      throw new IllegalStateException("Cannot copy: Bitmap is not initialized.")
    }

    val copyBitmap = new BufferedImage(WIDTH, HEIGHT, mBitmap.getType)

    val g = copyBitmap.createGraphics()
    g.drawImage(mBitmap, 0, 0, null)
    g.dispose()

    val copyName = name + "_copy"
    val copyInstance = new BetterGraphicsBitmap(copyName, copyBitmap)
    copyInstance
  }
}
