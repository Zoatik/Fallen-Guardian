import hevs.graphics.utils.GraphicsBitmap

class Sprite(var imagePath: String, var scale: Double = 1, var angle: Double = 0, var layerZ: Int = 0) {
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