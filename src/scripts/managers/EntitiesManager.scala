package scripts.managers

import scripts.gameClasses._
import scripts.data.Constants
import scripts.tools.{Audio, Renderer}

import scala.collection.mutable
import scala.util.Random

object EntitiesManager {

  val enemies: mutable.ListBuffer[Enemy] = mutable.ListBuffer()
  val towers: mutable.ListBuffer[Tower] = mutable.ListBuffer()
  val bullets: mutable.ListBuffer[Bullet] = mutable.ListBuffer()

  var player: Option[Player] = Some(new Player())
  var base: Option[Base] = Some(new Base())

  var waveTimer: Long = 0
  private var startTime: Long = 0
  private var waveCounter: Int = 0
  private var prevSpawnTime: Long = 0
  private val rand: Random = new Random()
  private var maxEnemies = 20

  var enemiesKilled: Int = 0

  private def spawnEnemy(pos: (Int, Int)): Unit = {
    val oscour: Enemy = new Enemy(pos, waveCounter)
    enemies += oscour
    oscour.target = base
  }

  def addBuilding(cell: Cell, buildType: Int, lvl: Int): Unit = {
    if(buildType == Constants.BUILD_TOWER){
      towers += new Tower(cell.pos, lvl)
      cell.entityPlaced = Some(towers.last)
      towers.last.collisionBox2D.checkMouseCollision(GameManager.mouseX - Renderer.offsetX,
                                                      GameManager.mouseY - Renderer.offsetY)
    }
  }

  def spawnBullet(posOffset: (Int, Int), sourceTower: Tower): Unit = {
    bullets += new Bullet(posOffset, sourceTower)
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
    startTime = GameManager.gameTimer
    prevSpawnTime = 0
    waveTimer = 0
    this.waveCounter = waveCounter
    maxEnemies += 5 * (waveCounter - 1)
  }

  def updateWave(): Boolean = {
    waveTimer = GameManager.gameTimer - startTime
    if(waveTimer < Constants.WAVE_TIME) {
      if (enemies.length < maxEnemies) {
        val spawnChance: Int = math.pow(waveCounter, 2).toInt + waveTimer.toInt / 1000

        val randNum = rand.nextInt(5000)
        if (waveTimer - prevSpawnTime > 100 && (randNum < spawnChance || enemies.length < 1)) {
          this.spawnEnemy(findSpawnPoint())
          prevSpawnTime = waveTimer
        }
      }
      return true
    }

    else if(enemies.isEmpty)
      return false
    true
  }

  def destroyEntity(entity: Entity): Unit = {
    entity match {
      case enemy: Enemy =>
        enemies -= enemy
        player.getOrElse(return).target = None
        towers.foreach(_.target = None)
        bullets.foreach(bullet => bullet.destroy())
        bullets.clear()
        enemiesKilled += 1
      case tower: Tower =>
        towers -= tower
        enemies.foreach(enemy => enemy.target = this.base)
      case bullet: Bullet =>
        bullets -= bullet
        towers.foreach(_.target = None)
      case base: Base =>
        enemies.foreach(enemy => {
          if(enemy.target.isDefined && enemy.target.get == base)
            enemy.target = None
        })
        this.base = None
        GameManager.gameOver(false)
      case player: Player =>
        enemies.foreach(enemy => {
          if(enemy.target.isDefined && enemy.target.get == player)
            enemy.target = this.base
        })
        playerKilled()
      case _ =>
        println("Unsupported entity type for destruction.")
    }
    entity.destroy()
  }

  private def playerKilled(): Unit = {
    if(base.isEmpty) {
      player.get.destroy()
      this.player = None
    }
    else {
      val newLvl: Int = if (player.get.getLvl > 1) player.get.getLvl - 1 else 1
      val newPos: (Int, Int) = base.get.getPosition()
      player = Some(new Player(_pos = newPos, _lvl = newLvl))
      new Audio(Constants.PLAYER_DEATH_AUDIO).play()
    }
  }

  var prevUpdateTime: Long = 0
  var prevCoinTime: Long = 0
  /**
   * Updates the CharacterMovements,
   */
  def updateActions() : Unit = {
    val currentTime = GameManager.gameTimer
    if(currentTime - prevCoinTime > 1000) {
      prevCoinTime = currentTime
      if(GameManager.isWavePlaying) {
        player.get.coins += 1
      }
    }

    if(currentTime - prevUpdateTime > 200) {
      if(!player.get.isAttacking)
        player.get.updateTargetPos()

      enemies.foreach( enemy => {
        if(!enemy.isAttacking)
          enemy.updateTargetPos()
      })

      bullets.foreach(bullet =>{
        bullet.refreshPath()
      })

      prevUpdateTime = currentTime
    }

    if (player.get.isMoving) {player.get.playerMoveAudio().play()}

    player.get.moveToTarget()
    player.get.tryToAttack()
    UiManager.updateUI()

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
