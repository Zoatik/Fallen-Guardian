package scripts.managers

import scripts.gameClasses._
import scripts.data.Constants
import scripts.data.Constants.pixelFont
import scripts.managers.EntitiesManager.{enemies, player, towers}
import scripts.tools.{Animation, BetterGraphicsBitmap, Layers, Sprite}

import java.awt.{Color, Font}
import scala.collection.mutable

object UiManager {

  private val staticUiElements: mutable.ListBuffer[StaticUiElement] = mutable.ListBuffer()

  /**------------MOBILE UI---------------**/
  def updateUI(): Unit = {
    checkPlayerUI()
    checkEnemyUI()
    checkTowerUI()
  }

  def checkPlayerUI(): Unit = {
    player.getOrElse(return).playerHealthBarSprite.setPosition(player.get.getAbsPosition._1, player.get.getAbsPosition._2 - 11)
    player.get.playerHealthBarSprite.changeImage(checkHealthBar(player.get))
  }
  def checkEnemyUI(): Unit = {
    enemies.foreach(enemy => {
      enemy.enemyHealthBarSprite.setPosition(enemy.getAbsPosition._1 + 11, enemy.getAbsPosition._2 - 11)
      enemy.enemyHealthBarSprite.changeImage(checkHealthBar(enemy))
      })
  }
  def checkTowerUI(): Unit = {
    towers.foreach(tower => {
      tower.towerHealthBarSprite.setPosition(tower.getAbsPosition._1, tower.getAbsPosition._2 - 22)
      tower.towerHealthBarSprite.changeImage(checkHealthBar(tower))
    })
  }

  def checkHealthBar(entity: Entity): BetterGraphicsBitmap = {
    if (entity.getHp >= 85 * entity.getMaxHp / 100) {
      entity match {
        case _: Player => Constants.PLAYER_HP_BAR_1
        case _: Enemy => Constants.ENEMY_HP_BAR_1
        case _: Tower => Constants.TOWER_HP_BAR_1
        case _ => Constants.PLAYER_HP_BAR_1
      }
    }
    else if (entity.getHp >= 75 * entity.getMaxHp / 100) {
      entity match {
        case _: Player => Constants.PLAYER_HP_BAR_2
        case _: Enemy => Constants.ENEMY_HP_BAR_2
        case _: Tower => Constants.TOWER_HP_BAR_2
        case _ => Constants.PLAYER_HP_BAR_2
      }
    }
    else if (entity.getHp >= 50 * entity.getMaxHp / 100) {
      entity match {
        case _: Player => Constants.PLAYER_HP_BAR_3
        case _: Enemy => Constants.ENEMY_HP_BAR_3
        case _: Tower => Constants.TOWER_HP_BAR_3
        case _ => Constants.PLAYER_HP_BAR_3
      }
    }
    else if (entity.getHp >= 20 * entity.getMaxHp / 100) {
      entity match {
        case _: Player => Constants.PLAYER_HP_BAR_4
        case _: Enemy => Constants.ENEMY_HP_BAR_4
        case _: Tower => Constants.TOWER_HP_BAR_4
        case _ => Constants.PLAYER_HP_BAR_4
      }
    }
    else {
      entity match {
        case _: Player => Constants.PLAYER_HP_BAR_5
        case _: Enemy => Constants.ENEMY_HP_BAR_5
        case _: Tower => Constants.TOWER_HP_BAR_5
        case _ => Constants.PLAYER_HP_BAR_5
      }
    }
  }

  /**------------STATIC UI---------------**/
  /**
   * Calls the functions bound to all staticUiElement
   */
  def updateLogics(): Unit = {
    staticUiElements.foreach(_.playLogics())
  }

  /**
   * Creates a StaticUIElement
   * @param sprite sprite
   * @param z layer height
   * @param textStr string text
   * @param offsetX text offset x
   * @param offsetY text offset y
   * @param color text color
   * @param font text font
   * @return StaticUiElement
   */
  def createUiElement(sprite: Sprite,
                      z: Int, textStr: String = "",
                      offsetX: Int = 0,
                      offsetY: Int = 0,
                      color: Color = Color.BLACK,
                      font: Font = pixelFont): StaticUiElement = {
    val uiElement: StaticUiElement = new StaticUiElement(sprite, new Ui_text(textStr, offsetX, offsetY, color, font))
    Layers.addStaticUiElement(z, uiElement)
    staticUiElements += uiElement
    uiElement
  }

  /**
   * Ui text class
   * @param text string text
   * @param offsetX text offset x
   * @param offsetY text offset y
   * @param color text color
   * @param font text font
   */
  class Ui_text(var text: String = "",
                var offsetX: Int = 0,
                var offsetY: Int = 0,
                var color: Color = Color.BLACK,
                var font: Font = pixelFont){}

  /**
   * Static UI element class
   * @param sprite sprite
   * @param text UI text
   */
  class StaticUiElement(var sprite : Sprite, var text: Ui_text = new Ui_text()) {
    val logics: mutable.ListBuffer[() => Unit] = mutable.ListBuffer()
    val animations: mutable.Map[String, Animation] = mutable.Map()
    def addLogic(f: () => Unit): Unit = {
      logics += f
    }

    def hide(): Unit = {
      Layers.removeStaticUiElement(this)
    }

    def show(z: Int): Unit = Layers.addStaticUiElement(z, this)

    def playLogics(): Unit = {
      logics.foreach(_())
    }

    def addAnimation(id: String, newAnimation: Animation): Unit = animations.update(id, newAnimation)

    def playAnimation(id: String): Unit = {
      stopAllAnimations()
      val anim = animations.getOrElse(id,{println(s"animation $id not found"); return})
      anim.activate()
      anim.play()
    }

    def stopAnimation(id: String): Unit = {
      animations.getOrElse(id, return).stop()
      animations(id).deactivate()
    }

    def stopAllAnimations(): Unit = {
      animations.keys.foreach(id => stopAnimation(id))
    }

    def isAnimationPlaying(): Boolean = {
      for(anim <- animations.values){
        if(anim.playing)
          return true
      }
      false
    }
    def isAnimationPlaying(id: String): Boolean = {
      animations.contains(id) && animations(id).playing
    }

  }

}
