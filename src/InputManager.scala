import scala.collection.mutable

object InputManager {
  private val bindings: mutable.Map[Int, mutable.ListBuffer[() => Unit]] = mutable.Map()
  private val keysDown: mutable.ListBuffer[Int]  = mutable.ListBuffer()
  private val keysEventBuffer: mutable.Queue[(Int, Boolean)] = mutable.Queue()


  // Binds a key to a function
  def bind(keyCode: Int, f: => Unit): Unit = {
    val actions = bindings.getOrElseUpdate(keyCode, mutable.ListBuffer())
    actions += (() => f) // Ajoute la fonction à la liste des bindings pour cette touche
  }


  // Methods for Keys handling
  def handleKeyPressed(keyCode: Int): Unit = {
    if(!keysDown.contains(keyCode))
      keysEventBuffer.enqueue((keyCode, true))
  }

  def handleKeyReleased(keyCode: Int): Unit = {
    if(keysDown.contains(keyCode))
      keysEventBuffer.enqueue((keyCode, false))
  }

  def handleKeys(): Unit = {
    while(keysEventBuffer.nonEmpty){
      val nextEvent: (Int, Boolean) = keysEventBuffer.dequeue()
      if(nextEvent._2 && !keysDown.contains(nextEvent._1))
        keysDown += nextEvent._1
      else if(!nextEvent._2 && keysDown.contains(nextEvent._1))
        keysDown -= nextEvent._1
    }

    for (keyCode <- keysDown) {
      bindings.get(keyCode).foreach(_.foreach(f => f()))
    }
  }
}