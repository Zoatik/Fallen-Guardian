import scala.collection.mutable

object BetterAudio {
  val audioList: mutable.Map[String, BetterAudio] = mutable.Map()

  private def addAudio(id: String, betterAudio: BetterAudio): Unit = audioList.update(id, betterAudio)
  private def removeAudio(id: String): Unit = audioList.remove(id)

  def stop(id: String): Unit = {
    audioList.getOrElse(id, return).stop()
    removeAudio(id)
  }

  def stopAll(): Unit = {
    audioList.foreach(_._2.audioClip.stop())
    audioList.clear()
  }

  def playNewAudio(id: String, betterAudio: BetterAudio, count: Int = -2): Unit = {
    audioList.toList.foreach(el => if(el._2.audioClip.isOpen) stop(el._1))
    addAudio(id, betterAudio)
    if(count >= -1)
      betterAudio.play(count)
    else
      betterAudio.play()
  }
}


class BetterAudio (path: String) extends Audio(path){

  def play(count: Int): Unit = {
    try {
      if (!audioClip.isOpen) audioClip.open()
      audioClip.stop()
      audioClip.setMicrosecondPosition(0)
      audioClip.loop(count)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  def stop(): Unit = {
    try {
      if (!audioClip.isOpen) return
      audioClip.stop()
      audioClip.close()
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }
}
