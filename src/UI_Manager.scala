import EntitiesManager.{player, enemies, towers}

object UI_Manager {

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

  def createUiElement(sprite: Sprite, z: Int, textStr: String = "", offsetX: Int = 0, offsetY: Int = 0): Unit = {
    val uiElement: StaticUiElement = {
      if(textStr.nonEmpty)
        new StaticUiElement(sprite, new Ui_text(textStr, offsetX, offsetY))
      else
        new StaticUiElement(sprite)
    }

    Layers.addStaticUiElement(z, uiElement)
  }

  class Ui_text(var text: String = "", var offsetX: Int = 0, var offsetY: Int = 0){}
  class StaticUiElement(var sprite : Sprite, var text: Ui_text = new Ui_text()){}


}
