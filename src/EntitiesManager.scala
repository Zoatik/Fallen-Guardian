import java.awt.event.KeyEvent
import scala.collection.mutable
import scala.util.Random

object EntitiesManager {

  var enemies: mutable.ListBuffer[Enemy] = mutable.ListBuffer()
  var towers: mutable.ListBuffer[Tower] = mutable.ListBuffer()

  var player: Player = new Player()

  var tower: Tower = new Tower()
  towers += tower
  //println(tower.getAbsPosition)

  private var waveTimer: Long = 0
  private var startTime: Long = 0
  private var waveCounter: Int = 0
  private var prevSpawnTime: Long = 0
  private val rand: Random = new Random()
  private val maxEnemies = 20

  // DEBUG
  InputManager.bindKey(KeyEvent.VK_Q, (_, pressed) => if(!pressed) spawnEnemy((20,20)))
  InputManager.bindKey(KeyEvent.VK_E, (_, pressed) => if(!pressed) destroyEntity(enemies.head))

  private def spawnEnemy(pos: (Int, Int)): Unit = {
    val oscour: Enemy = new Enemy(pos, 1)
    enemies += oscour
    oscour.calculatePath(30,30)
  }

  private def findSpawnPoint(): (Int, Int) = {
    val maxX: Int = Constants.GRID_SIZE
    val maxY: Int = Constants.GRID_SIZE
    var spawnPoint = (rand.nextInt(maxX), rand.nextInt(maxX))
    while(math.pow(spawnPoint._1- maxX/2, 2) + math.pow(spawnPoint._1- maxY/2, 2) < 100){
      spawnPoint = (rand.nextInt(59), rand.nextInt(59))
    }
    spawnPoint
  }

  def startWave(waveCounter: Int): Unit = {
    startTime = System.currentTimeMillis()
    waveTimer = 0
    this.waveCounter = waveCounter
  }

  def updateWave(): Unit = {
    waveTimer = System.currentTimeMillis() - startTime
    if(enemies.length < maxEnemies) {

      val spawnChance: Int = math.pow(waveCounter, 2).toInt + waveTimer.toInt / 10000

      val randNum = rand.nextInt(5000)
      if (waveTimer - prevSpawnTime > 100 && (randNum < spawnChance || enemies.length < 1)) {
        this.spawnEnemy(findSpawnPoint())
        prevSpawnTime = waveTimer
        println(enemies.length)
      }
    }
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
    //tower.findTarget()
    tower.towerTryToAttack()

    
    enemies.foreach(enemy => {
      enemy.tryToAttack()
      enemy.moveToTarget()
    })
  }
}