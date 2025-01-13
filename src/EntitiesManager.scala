import java.awt.event.KeyEvent
import scala.collection.mutable
import scala.util.Random

object EntitiesManager {

  val enemies: mutable.ListBuffer[Enemy] = mutable.ListBuffer()
  val towers: mutable.ListBuffer[Tower] = mutable.ListBuffer()
  val bullets: mutable.ListBuffer[Bullet] = mutable.ListBuffer()

  var player: Player = new Player()
  var base: Option[Base] = Some(new Base())


  Constants.THEME_SONG.audioClip.loop(999999)


  var waveTimer: Long = 0
  private var startTime: Long = 0
  private var waveCounter: Int = 0
  private var prevSpawnTime: Long = 0
  private val rand: Random = new Random()
  private val maxEnemies = 20

  // DEBUG
  InputManager.bindKey(KeyEvent.VK_Q, (_, pressed) => if(!pressed) spawnEnemy((20,20)))
  InputManager.bindKey(KeyEvent.VK_E, (_, pressed) => if(!pressed) destroyEntity(enemies.head))

  private def spawnEnemy(pos: (Int, Int)): Unit = {
    val oscour: Enemy = new Enemy(pos, waveCounter)
    enemies += oscour
    oscour.target = base
  }

  def addBuilding(pos: (Int, Int), buildType: Int, lvl: Int): Unit = {
    if(buildType == Constants.BUILD_TOWER){
      towers += new Tower(pos, lvl)
    }
  }

  def spawnBullet(pos: (Int, Int), sourceTower: Tower): Unit = {
    bullets += new Bullet((0,-1), sourceTower)
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

  def updateWave(): Boolean = {
    waveTimer = System.currentTimeMillis() - startTime
    if(waveTimer < Constants.WAVE_TIME) {
      if (enemies.length < maxEnemies) {

        val spawnChance: Int = math.pow(waveCounter, 2).toInt + waveTimer.toInt / 10000

        val randNum = rand.nextInt(5000)
        if (waveTimer - prevSpawnTime > 100 && (randNum < spawnChance || enemies.length < 1)) {
          this.spawnEnemy(findSpawnPoint())
          prevSpawnTime = waveTimer
        }
      }
      return true
    }

    else if(enemies.isEmpty) // returns false only when the time is up and no more enemies are alive
      return false
    true
  }

  def destroyEntity(entity: Entity): Unit = {
    entity match {
      case enemy: Enemy =>
        enemies -= enemy
        player.target = None
        towers.foreach(_.target = None)
      case tower: Tower =>
        towers -= tower
        player.target = None
      case bullet: Bullet =>
        bullets -= bullet
        towers.foreach(_.target = None)
        player.target = None
      case base: Base =>
        enemies.foreach(enemy => {
          if(enemy.target.get == base)
            enemy.target = None
        })
        this.base = None
        GameManager.gameOver(false)
      case _ =>
        println("Unsupported entity type for destruction.")
    }
    entity.destroy()
  }

  var prevUpdateTime: Long = 0
  var prevCoinTime: Long = 0
  /**
   * Updates the CharacterMovements,
   */
  def updateActions() : Unit = {
    val currentTime = System.currentTimeMillis()
    if(currentTime - prevCoinTime > 1000) {
      prevCoinTime = currentTime
      player.coins += 1
    }

    if(currentTime - prevUpdateTime > 200) {
      if(!player.isAttacking)
        player.updateTargetPos()

      enemies.foreach( enemy => {
        if(!enemy.isAttacking)
          enemy.updateTargetPos()
      })

      bullets.foreach(bullet =>{
        bullet.refreshPath()
      })

      prevUpdateTime = currentTime
    }

    if(player.isMoving){Constants.PLAYER_MOVE_AUDIO.play()}

    player.moveToTarget()
    player.tryToAttack()

    towers.foreach(tower => {
      tower.towerTryToAttack()
    })
    bullets.toList.foreach(bullet => {
      bullet.moveToTarget()
    })
    
    enemies.foreach(enemy => {
      enemy.tryToAttack()
      enemy.moveToTarget()
    })
  }
}