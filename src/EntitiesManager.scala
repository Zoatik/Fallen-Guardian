import java.awt.event.KeyEvent
import scala.collection.mutable

object EntitiesManager {

  var enemies: mutable.ListBuffer[Enemy] = mutable.ListBuffer()

  var player: Player = new Player()

  InputManager.bindKey(KeyEvent.VK_Q, (_, pressed) => if(!pressed) spawnEnemy())
  InputManager.bindKey(KeyEvent.VK_E, (_, pressed) => if(!pressed) destroyEntity(enemies.head))

  private def spawnEnemy(): Unit = {
    val oscour: Enemy = new Enemy(
      _pos = (20,20),
      _hp = 20,
      _baseImagePath = "/res/Characters/Enemy/Orc/idle/orcIdle_0.png",
      _velocity = 1.5,
      _damage = 1,
      _armor = 2
    )
    enemies += oscour
    oscour.calculatePath(50,50)
  }


  def destroyEntity(entity: Entity): Unit = {
    entity match {
      case e: Enemy =>
        enemies -= e
        player.target = None
      case _ =>
        println("Unsupported entity type for destruction.")
    }
    entity.destroy()
  }

  var prevUpdateTime: Long = 0
  var prevMovementTime: Long = 0
  /**
   * Updates the CharacterMovements,
   */
  def updateActions() : Unit = {
    if(System.currentTimeMillis() - prevUpdateTime > 200) {
      if(!player.isAttacking)
        player.updateTargetPos()

      enemies.foreach( enemy => {
        if(!enemy.isAttacking)
          enemy.updateTargetPos()
      })

      prevUpdateTime = System.currentTimeMillis()
    }

    player.moveToTarget()
    player.tryToAttack()
    enemies.foreach(enemy => {
      enemy.moveToTarget()
      enemy.tryToAttack()
    })

  }

}