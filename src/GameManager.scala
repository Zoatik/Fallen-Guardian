import Constants._
import Main._

import java.awt.event.{KeyEvent, MouseEvent}
import InputManager._

import Character._

object GameManager {

  /** Player creation * */
  var aled: Player = new Player()
  //var testSprite: Sprite = new Sprite("/res/Characters/Soldier/idle/soldierIdle0.png", (aled.getPosition()._1, aled.getPosition()._2))
  layers.addSprite(1, aled.sprite)

  /** TEST Animation * */
  var anim: Animation = new Animation(aled.sprite, AnimationsResources.ANIM_SOLDIER_IDLE, 1000, true)

  anim.play()
  bindKey(KeyEvent.VK_C, pressed => anim.play())
  bindKey(KeyEvent.VK_V, pressed => anim.stop())


  def handleCellAction(mouseButton: Int, pressed: Boolean, cell: Cell): Unit = {
    if (!pressed){
      if(mouseButton == MouseEvent.BUTTON3) {
        aled.calculatePath(cell.pos._1, cell.pos._2)
      }
    }
  }

  def update() : Unit = {
    aled.moveToTarget()
  }

}
