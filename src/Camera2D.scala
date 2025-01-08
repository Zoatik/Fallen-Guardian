import GameManager.{mouseX, mouseY}

/**
 * Defines a 2D topDown camera for the funGraphics library
 * @param up    key code for up direction
 * @param down  key code for down direction
 * @param left  key code for left direction
 * @param right key code for right direction
 */
class Camera2D(var up: Int, var down: Int, var left: Int, var right: Int) {
  /*--- binds the keys to the camera movements ---*/
  InputManager.bindKey(up, (keyCode, isPressed) => handleInputs(keyCode, isPressed))
  InputManager.bindKey(down, (keyCode, isPressed) => handleInputs(keyCode, isPressed))
  InputManager.bindKey(left, (keyCode, isPressed) => handleInputs(keyCode, isPressed))
  InputManager.bindKey(right, (keyCode, isPressed) => handleInputs(keyCode, isPressed))

  /*--- binds the keys to the mouse collision check function ---*/
  InputManager.bindKey(up, (_, _) => {
    CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY)
  })
  InputManager.bindKey(down, (_, _) => {
    CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY)
  })
  InputManager.bindKey(left, (_, _) => {
    CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY)
  })
  InputManager.bindKey(right, (_, _) => {
    CollisionBox2DManager.checkMouseCollisions(mouseX - Renderer.offsetX, mouseY - Renderer.offsetY)
  })

  def this(){
    this(
      up = Constants.CAMERA_UP,
      down = Constants.CAMERA_DOWN,
      left = Constants.CAMERA_LEFT,
      right = Constants.CAMERA_RIGHT,
    )
  }

  /**
   * Handles Camera2D movement Inputs
   * @param keyCode   key code of the eventKey
   * @param isPressed true if the key is pressed, false if the key is released
   */
  private def handleInputs(keyCode: Int, isPressed: Boolean): Unit = {
    if(!isPressed)
      return
    if(keyCode == left) {
      Renderer.move(1,0, Renderer.deltaT.toInt)
    } else if (keyCode == right) {
      Renderer.move(-1,0, Renderer.deltaT.toInt)
    } else if (keyCode == down){
      Renderer.move(0,-1, Renderer.deltaT.toInt)
    } else if (keyCode == up) {
      Renderer.move(0,1, Renderer.deltaT.toInt)
    }
  }

}
