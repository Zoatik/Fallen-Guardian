import EntitiesManager.{player, enemies, towers}

object UI_Manager {

  def updateUI(): Unit = {
    checkPlayerUI()
    checkEnemyUI()
  }


  def checkPlayerUI(): Unit = {
    player.getOrElse(return).playerHealthBarSprite.setPosition(player.get.getAbsPosition._1, player.get.getAbsPosition._2 - 11)
    player.get.playerHealthBarSprite.changeImage(checkHealthBar(player.get.getHp(), player.get.getMaxHp()))
  }
  def checkEnemyUI(): Unit = {
    enemies.foreach(enemy=>{
      enemy.enemyHealthBarSprite.setPosition(enemy.getAbsPosition._1, enemy.getAbsPosition._2)
      enemy.enemyHealthBarSprite.changeImage(checkHealthBar(enemy.getHp(), enemy.getMaxHp()))
    })
   }


  def checkHealthBar(currentHealth: Int, maxHealth: Int): BetterGraphicsBitmap = {
    if (currentHealth >= 85 * maxHealth / 100) {
      Constants.PLAYER_HP_BAR_1
    }
    else if (currentHealth >= 75 * maxHealth / 100) {
      Constants.PLAYER_HP_BAR_2
    }
    else if (currentHealth >= 50 * maxHealth / 100) {
      Constants.PLAYER_HP_BAR_3
    }
    else if (currentHealth >= 20 * maxHealth / 100) {
      Constants.PLAYER_HP_BAR_4
    }
    else {
      Constants.PLAYER_HP_BAR_5
    }
  }

  case class Ui_text(var text: String = "", var offsetX: Int = 0, var offsetY: Int = 0){}
  
  class StaticUiElement(var sprite : Sprite, var text: Ui_text = Ui_text()){}


}
