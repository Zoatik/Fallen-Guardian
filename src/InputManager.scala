import java.awt.event.MouseEvent
import scala.collection.mutable

/**
 * Custom Input Manager that handles :
 *
 * 1. Key bindings
 *
 * 2. Mouse button bindings
 *
 * 3. Mouse motion event bindings
 *
 * @note multiple functions can be bound to one event code
 */
object InputManager {
  private val keyBindings: mutable.Map[Int, mutable.ListBuffer[(Int, Boolean) => Unit]] = mutable.Map()
  private val mouseBindings: mutable.Map[Int, mutable.ListBuffer[(Int, Boolean) => Unit]] = mutable.Map()
  private val mouseMotionBindings: mutable.Set[(Int, Int) => Unit] = mutable.Set()
  private val activeKeys: mutable.Set[Int] = mutable.Set()
  private val activeMouseButtons: mutable.Set[Int] = mutable.Set()
  private val keysEventBuffer: mutable.Queue[(Int, Boolean)] = mutable.Queue()
  private val mouseButtonsEventBuffer: mutable.Queue[(Int, Boolean)] = mutable.Queue()
  private val mouseMotionEventBuffer: mutable.Queue[(Int, Int)] = mutable.Queue()


  /**
   * Binds a key to a function
   * @param keyCode Key code
   * @param f       Function to bind
   */
  def bindKey(keyCode: Int, f: (Int, Boolean) => Unit): Unit = {
    val actions = keyBindings.getOrElseUpdate(keyCode, mutable.ListBuffer())
    actions += f // Ajoute la fonction à la liste des bindings pour cette touche
  }

  /**
   * Binds a mouse button to a function
   * @param mouseButton Mouse button code
   * @param f           Function to bind
   */
  def bindMouseButton(mouseButton: Int, f: (Int, Boolean) => Unit): Unit = {
    val actions = mouseBindings.getOrElseUpdate(mouseButton, mutable.ListBuffer())
    actions += f
  }

  /**
   * Binds a mouse Motion event to a function
   * @param f Function to bind
   */
  def bindMouseMotion(f: (Int, Int) => Unit): Unit = {
    mouseMotionBindings += f
  }


  /**
   * Adds the key pressed event to the keysEventBuffer
   * @param keyCode Key pressed code
   * @note Must be called when event is triggered
   */
  def handleKeyPressed(keyCode: Int): Unit = {
    if(keysEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      if (!activeKeys.contains(keyCode)) {
        keysEventBuffer.enqueue((keyCode, true)) // Ajouter à la queue
        activeKeys += keyCode // Marquer la touche comme active
      }
    }
  }

  /**
   * Adds the key released event to the keysEventBuffer
   * @param keyCode Key released code
   * @note Must be called when event is triggered
   */
  def handleKeyReleased(keyCode: Int): Unit = {
    if(keysEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      if (activeKeys.contains(keyCode)) {
        keysEventBuffer.enqueue((keyCode, false)) // Ajouter l'événement de relâchement à la queue
        activeKeys -= keyCode // Retirer la touche de l'ensemble
      }
    }
  }

  /**
   * Handles the buffered key events
   * @note Should be called in the main Loop
   */
  def handleKeys(): Unit = {
    while(keysEventBuffer.nonEmpty){
      val nextEvent: (Int, Boolean) = keysEventBuffer.dequeue()
      keyBindings.get(nextEvent._1).foreach(_.foreach(f => f(nextEvent._1, nextEvent._2)))

    }
    for(activeKey <- activeKeys){
      keysEventBuffer.enqueue((activeKey, true))
    }

  }

  /**
   * Adds mouse button pressed event to the mouseButtonsEventBuffer
   * @param mouseButton Mouse button code
   * @note Must be called when the event is triggered
   */
  def handleMouseButtonPressed(mouseButton: Int): Unit = {
    if(mouseButtonsEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      if (!activeMouseButtons.contains(mouseButton)) {
        mouseButtonsEventBuffer.enqueue((mouseButton, true)) // Ajouter à la queue
        activeMouseButtons += mouseButton // Marquer la touche comme active
      }
    }
  }

  /**
   * Adds the mouse button released event to the mouseButtonsEventBuffer
   * @param mouseButton Mouse button code
   * @note Must be called when the event is triggered
   */
  def handleMouseButtonReleased(mouseButton: Int): Unit = {
    if(mouseButtonsEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      if (activeMouseButtons.contains(mouseButton)) {
        mouseButtonsEventBuffer.enqueue((mouseButton, false)) // Ajouter à la queue
        activeMouseButtons -= mouseButton // Marquer la touche comme active
      }
    }
  }

  /**
   * Adds the mouse motion event to the mouseMotionEventBuffer
   * @param x Absolute mouse position X
   * @param y Absolute mouse position Y
   * @note Must be called when the event is triggered
   */
  def handleMouseMoved(x: Int, y: Int): Unit = {
    if(mouseMotionEventBuffer.length < Constants.MAX_INPUT_BUFFER_SIZE) {
      mouseMotionEventBuffer.enqueue((x,y))
    }
  }

  /**
   * Handles the mouse events
   * @note Should be called in the main Loop
   */
  def handleMouse(): Unit = {
    while(mouseButtonsEventBuffer.nonEmpty){
      val nextEvent: (Int, Boolean) = mouseButtonsEventBuffer.dequeue()
      if (nextEvent != null)
        mouseBindings.get(nextEvent._1).foreach(_.foreach(f => f(nextEvent._1, nextEvent._2)))

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