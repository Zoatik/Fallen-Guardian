import EntitiesManager.{player, enemies}

class UI_Manager {

  var playerHealthBarSprite: Sprite = new Sprite(Constants.PLAYER_HP_BAR_1)
  var enemyHealthBarSprite: Sprite = new Sprite(Constants.PLAYER_HP_BAR_1)

  def updateUI(): Unit = {
    checkPlayerUI()
    checkEnemyUI()
  }

  def initPlayerUI(): Unit = {Layers.addSprite(Constants.LAYER_UI_MOBILE, playerHealthBarSprite)}
  def initEnemyUI(): Unit = {Layers.addSprite(Constants.LAYER_UI_MOBILE, enemyHealthBarSprite)}


  def checkPlayerUI(): Unit = {
    playerHealthBarSprite.setPosition(player.get.getAbsPosition._1, player.get.getAbsPosition._2 - 11)
    playerHealthBarSprite.changeImage(checkHealthBar(player.get.getHp(), player.get.getMaxHp()))
  }
  def checkEnemyUI(): Unit = {
    enemies.foreach(enemy=>{
      enemy.sprite.setPosition(enemy.getAbsPosition._1, enemy.getAbsPosition._2)
      enemy.sprite.changeImage(checkHealthBar(enemy.getHp(), enemy.getMaxHp()))
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


}
