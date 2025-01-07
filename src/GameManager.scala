import Constants._
import Main._

import java.awt.event.{KeyEvent, MouseEvent}
import InputManager._

import Character._

object GameManager {
  """
    Je pensais faire une fonction de spawn au début,
    mais ca me faiais chier pour le reste de l'update.
    Genre si je voulais déplacer la position du jouer (et de son sprite/animation),
    j'y avais pas accès avant.

    def spawnPlayer() : Unit = {
      /** Character creation * */
      var player: Entity = new Entity(defaultPos, defaultHp, defaultArmor, defaultSprite, defaultBoxCollision2D)

      /** TEST Animation * */
      var images = AnimationsResources.ANIM_SOLDIER_IDLE
      val testSprite: Sprite = new Sprite(images.head, (defaultPos._1, defaultPos._2))
      layers.addSprite(1, testSprite)
      var anim: Animation = new Animation(testSprite, images, 1000, true)

      anim.play()
      bindKey(KeyEvent.VK_C, pressed => anim.play())
      bindKey(KeyEvent.VK_V, pressed => anim.stop())
      //////////////////////////////////////////////////////////////////////////////////////////////////////
      println("The player has entered the arena.")
    }
  """
  //Dis do be wrong tho
  //var pp : Entity = new Entity((60, 60), 3, 3, defaultSprite, defaultBoxCollision2D)



  //** -------------------------------------------------------------------------------------------------------------* */
                                                //Movement Tests
  /** Player creation * */
  var aled: Player = new Player((160, 160), 3, 3, defaultSprite, defaultBoxCollision2D, (6, 6), 26)
  var testSprite: Sprite = new Sprite("/res/Characters/Soldier/idle/soldierIdle0.png", (aled.getPos()._1, aled.getPos()._2))
  layers.addSprite(1, testSprite)

  /** TEST Animation * */
  var images = AnimationsResources.ANIM_SOLDIER_IDLE
  val animTestSprite: Sprite = new Sprite(images.head, (aled.getPos()._1, aled.getPos()._2))
  layers.addSprite(1, animTestSprite)
  var anim: Animation = new Animation(animTestSprite, images, 1000, true)

  anim.play()
  bindKey(KeyEvent.VK_C, pressed => anim.play())
  bindKey(KeyEvent.VK_V, pressed => anim.stop())

  var compteur : Int = 32

  def moveDiag(sign : Int) : Unit = {
    aled.setPosition(sign*compteur, sign*compteur)
      //en supprimant le code ci-dessous, ca l'a optimisé 100x plus (logique bro)
    //val testSprite1: Sprite = new Sprite("/res/cellEmpty_2.png", (aled.getPos()._1, aled.getPos()._2))
    testSprite.setPosition((aled.getPos()._1, aled.getPos()._2))
    animTestSprite.setPosition((aled.getPos()._1, aled.getPos()._2))
  }

  def update() : Unit = {
    /** Moving function tests * */
    bindKey(KeyEvent.VK_UP, pressed => println("Up"))
    bindKey(KeyEvent.VK_DOWN, pressed => println("Down"))
    bindKey(KeyEvent.VK_RIGHT, pressed => moveDiag(1))
    bindKey(KeyEvent.VK_RIGHT, pressed => compteur += 1)
    bindKey(KeyEvent.VK_LEFT, pressed => moveDiag(-1))
    bindKey(KeyEvent.VK_LEFT, pressed => compteur -= 1)
    bindMouseButton(MouseEvent.BUTTON2, pressed => compteur += 32)

  }

}
