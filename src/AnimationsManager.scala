import scala.collection.mutable

object AnimationsManager {
  private val animationsBuffer: mutable.ListBuffer[Animation] = mutable.ListBuffer()

  def add(newAnim: Animation): Unit = this.animationsBuffer += newAnim

  def remove(animToRemove: Animation): Unit = this.animationsBuffer -= animToRemove

  def removeAll(): Unit = animationsBuffer.foreach(_.deactivate())

  def run(): Unit = animationsBuffer.foreach(_.next())

  def stopAll(): Unit = animationsBuffer.foreach(_.stop())

  def playAll(): Unit = animationsBuffer.foreach(_.play())
}

class Animation( var spriteTarget: Sprite,
                 var imagesPathBuffer: mutable.ListBuffer[String],
                 var duration: Int,
                 var loop: Boolean = true,
                 private var active: Boolean = true
               ) {
  var indexCounter: Int = 0
  var prevTime: Long = 0
  var finished: Boolean = false
  var playing: Boolean = false

  if (active)
    this.activate()


  def this(spriteTarget: Sprite) {
    this(spriteTarget, mutable.ListBuffer.empty, 0, false, false)
  }

  def init( imagesPathBuffer: mutable.ListBuffer[String],
            duration: Int,
            loop: Boolean = true,
            active: Boolean = true
          ): Unit = {
    this.imagesPathBuffer = imagesPathBuffer
    this.duration = duration
    this.loop = loop
    this.active = active
    if(active)
      this.activate()
  }

  def next(): Unit = {
    if(finished || !playing)
      return
    val minDeltaT: Int = duration / imagesPathBuffer.length
    if (System.currentTimeMillis() - prevTime > minDeltaT){
      nextIndex()
      val nextImage: String = imagesPathBuffer(indexCounter)
      this.spriteTarget.changeImage(nextImage)
      prevTime = System.currentTimeMillis()
    }

  }

  def play(fromStart: Boolean = true): Unit = {
    if(fromStart)
      indexCounter = 0
    prevTime = System.currentTimeMillis()
    finished = false
    playing = true
  }

  def stop(): Unit = {
    playing = false
  }

  def activate(): Unit = {
      AnimationsManager.add(this)
      active = true
  }
  def deactivate(): Unit = {
      AnimationsManager.remove(this)
      active = false
  }

  def isActive: Boolean = active

  private def nextIndex(): Unit = {
    indexCounter = (indexCounter + 1) % imagesPathBuffer.length
    if(indexCounter == 0 && !loop) {
      finished = true
      playing = false
    }
  }

}