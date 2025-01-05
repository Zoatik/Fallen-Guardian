import InputManager.bindKey
import Main.deltaT

import java.awt.event.KeyEvent

class Camera2D {
  bindKey(KeyEvent.VK_A, isPressed => handleInputs(KeyEvent.VK_A, isPressed))
  bindKey(KeyEvent.VK_D, isPressed => handleInputs(KeyEvent.VK_D, isPressed))
  bindKey(KeyEvent.VK_S, isPressed => handleInputs(KeyEvent.VK_S, isPressed))
  bindKey(KeyEvent.VK_W, isPressed => handleInputs(KeyEvent.VK_W, isPressed))

  def handleInputs(keyCode: Int, isPressed: Boolean): Unit = {
    if(!isPressed)
      return
    keyCode match {
      case KeyEvent.VK_A => Renderer.move(1,0, deltaT.toInt)
      case KeyEvent.VK_D => Renderer.move(-1,0, deltaT.toInt)
      case KeyEvent.VK_S => Renderer.move(0,-1, deltaT.toInt)
      case KeyEvent.VK_W => Renderer.move(0,1, deltaT.toInt)
      case _ => return
    }
  }

}
