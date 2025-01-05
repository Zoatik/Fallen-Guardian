import java.awt.event.MouseEvent
import scala.collection.mutable

object InputManager {
  private val keyBindings: mutable.Map[Int, mutable.ListBuffer[Boolean => Unit]] = mutable.Map()
  private val mouseBindings: mutable.Map[Int, mutable.ListBuffer[Boolean => Unit]] = mutable.Map()
  private val mouseMotionBindings: mutable.Set[(Int, Int) => Unit] = mutable.Set()
  private val activeKeys: mutable.Set[Int] = mutable.Set()
  private val activeMouseButtons: mutable.Set[Int] = mutable.Set()
  private val keysEventBuffer: mutable.Queue[(Int, Boolean)] = mutable.Queue()
  private val mouseButtonsEventBuffer: mutable.Queue[(Int, Boolean)] = mutable.Queue()
  private val mouseMotionEventBuffer: mutable.Queue[(Int, Int)] = mutable.Queue()


  // Binds a key to a function
  def bindKey(keyCode: Int, f: Boolean => Unit): Unit = {
    val actions = keyBindings.getOrElseUpdate(keyCode, mutable.ListBuffer())
    actions += f // Ajoute la fonction à la liste des bindings pour cette touche
  }

  def bindMouseButton(mouseButton: Int, f: Boolean => Unit): Unit = {
    val actions = mouseBindings.getOrElseUpdate(mouseButton, mutable.ListBuffer())
    actions += f
  }

  def bindMouseMotion(f: (Int, Int) => Unit): Unit = {
    mouseMotionBindings += f
  }



  // Methods for Keys handling
  def handleKeyPressed(keyCode: Int): Unit = {
    if(keysEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      if (!activeKeys.contains(keyCode)) {
        keysEventBuffer.enqueue((keyCode, true)) // Ajouter à la queue
        activeKeys += keyCode // Marquer la touche comme active
      }
    }
  }

  def handleKeyReleased(keyCode: Int): Unit = {
    if(keysEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      if (activeKeys.contains(keyCode)) {
        keysEventBuffer.enqueue((keyCode, false)) // Ajouter l'événement de relâchement à la queue
        activeKeys -= keyCode // Retirer la touche de l'ensemble
      }
    }
  }

  def handleKeys(): Unit = {
    while(keysEventBuffer.nonEmpty){
      val nextEvent: (Int, Boolean) = keysEventBuffer.dequeue()
      keyBindings.get(nextEvent._1).foreach(_.foreach(f => f(nextEvent._2)))

    }
    for(activeKey <- activeKeys){
      keysEventBuffer.enqueue((activeKey, true))
    }

  }

  def handleMouseButtonPressed(mouseButton: Int): Unit = {
    if(mouseButtonsEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      if (!activeMouseButtons.contains(mouseButton)) {
        mouseButtonsEventBuffer.enqueue((mouseButton, true)) // Ajouter à la queue
        activeMouseButtons += mouseButton // Marquer la touche comme active
      }
    }
  }

  def handleMouseButtonReleased(mouseButton: Int): Unit = {
    if(mouseButtonsEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      if (activeMouseButtons.contains(mouseButton)) {
        mouseButtonsEventBuffer.enqueue((mouseButton, false)) // Ajouter à la queue
        activeMouseButtons -= mouseButton // Marquer la touche comme active
      }
    }
  }

  def handleMouseMoved(x: Int, y: Int): Unit = {
    if(mouseMotionEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      mouseMotionEventBuffer.enqueue((x,y))
      //println(mouseMotionEventBuffer.length)
    }


  }

  def handleMouse(): Unit = {
    while(mouseButtonsEventBuffer.nonEmpty){
      val nextEvent: (Int, Boolean) = mouseButtonsEventBuffer.dequeue()
      mouseBindings.get(nextEvent._1).foreach(_.foreach(f => f(nextEvent._2)))

    }
    while(mouseMotionEventBuffer.nonEmpty){
      val nextPos: (Int, Int) = mouseMotionEventBuffer.dequeue()
      mouseMotionBindings.foreach(f => f(nextPos._1, nextPos._2))

    }

    for(activeMouseButton <- activeMouseButtons){
      mouseButtonsEventBuffer.enqueue((activeMouseButton, true))
    }
  }

}