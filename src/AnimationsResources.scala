import hevs.graphics.utils.GraphicsBitmap

import scala.collection.mutable

object AnimationsResources {

  val BITMAP_CELL_GRASS_0: GraphicsBitmap = new GraphicsBitmap("/res/ground/TX_grass_0.png")
  val BITMAP_CELL_GRASS_1: GraphicsBitmap = new GraphicsBitmap("/res/ground/TX_grass_1.png")
  val BITMAP_CELL_STONE_0: GraphicsBitmap = new GraphicsBitmap("/res/ground/TX_stone_0.png")
  val BITMAP_CELL_FLOWER_0: GraphicsBitmap = new GraphicsBitmap("/res/ground/TX_flower_0.png")
  val BITMAP_CELL_EMPTY: GraphicsBitmap = new GraphicsBitmap("/res/ground/cellEmpty.png")

  val ANIM_SOLDIER_IDLE: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Characters/Soldier/idle/soldierIdle_0.png"),
      new GraphicsBitmap("/res/Characters/Soldier/idle/soldierIdle_1.png"),
      new GraphicsBitmap("/res/Characters/Soldier/idle/soldierIdle_2.png")
    )}

  val ANIM_SOLDIER_HURT: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Characters/soldier/hurt/soldierHurt_0.png"),
      new GraphicsBitmap("/res/Characters/soldier/hurt/soldierHurt_1.png"),
      new GraphicsBitmap("/res/Characters/soldier/hurt/soldierHurt_2.png")
    )}

  val ANIM_SOLDIER_WALK: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_0.png"),
      //"/res/Characters/Soldier/walk/soldierWalk_1.png",
      new GraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_2.png"),
      //"/res/Characters/Soldier/walk/soldierWalk_3.png",
      new GraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_4.png"),
      //"/res/Characters/Soldier/walk/soldierWalk_5.png",
      new GraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_6.png"),
      new GraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_7.png")
    )}

  val ANIM_SOLDIER_ATTACK_1: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Characters/Soldier/attack1/SoldierAttack1_0.png"),
      new GraphicsBitmap("/res/Characters/Soldier/attack1/SoldierAttack1_1.png"),
      new GraphicsBitmap("/res/Characters/Soldier/attack1/SoldierAttack1_2.png"),
      new GraphicsBitmap("/res/Characters/Soldier/attack1/SoldierAttack1_3.png"),
      new GraphicsBitmap("/res/Characters/Soldier/attack1/SoldierAttack1_4.png"),
      new GraphicsBitmap("/res/Characters/Soldier/attack1/SoldierAttack1_5.png")
    )}


  val ANIM_ORC_IDLE: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_0.png"),
      new GraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_1.png"),
      new GraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_2.png")
    )}

  val ANIM_ORC_HURT: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Characters/Enemy/Orc/hurt/orcHurt_0.png"),
      new GraphicsBitmap("/res/Characters/Enemy/Orc/hurt/orcHurt_1.png"),
      new GraphicsBitmap("/res/Characters/Enemy/Orc/hurt/orcHurt_2.png")
    )}

  val ANIM_ORC_ATTACK_1: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_0.png"), // TO FIX A FLICKERING PROBLEM

      //"/res/Characters/Enemy/Orc/attack1/orcAttack1_0.png",
      //"/res/Characters/Enemy/Orc/attack1/orcAttack1_1.png",
      new GraphicsBitmap("/res/Characters/Enemy/Orc/attack1/orcAttack1_2.png"),
        new GraphicsBitmap("/res/Characters/Enemy/Orc/attack1/orcAttack1_3.png"),
      //"/res/Characters/Enemy/Orc/attack1/orcAttack1_4.png",
        new GraphicsBitmap("/res/Characters/Enemy/Orc/attack1/orcAttack1_5.png"),

    )}

  val ANIM_ORC_WALK: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_0.png"),
      //"/res/Characters/Enemy/Orc/walk/orcWalk_1.png",
      new GraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_2.png"),
      //"/res/Characters/Enemy/Orc/walk/orcWalk_3.png",
      new GraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_4.png"),
      //"/res/Characters/Enemy/Orc/walk/orcWalk_5.png",
      new GraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_6.png"),
      new GraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_7.png")
    )}

  val ANIM_BASE_HURT: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Base/base_0.png"),
      new GraphicsBitmap("/res/Base/base_1.png"),
      new GraphicsBitmap("/res/Base/base_2.png"),
      new GraphicsBitmap("/res/Base/base_1.png"),
      new GraphicsBitmap("/res/Base/base_2.png"),
      new GraphicsBitmap("/res/Base/base_0.png")
    )
  }

  val ANIM_TOWER_IDLE: Array[GraphicsBitmap] = {
    Array(
      new GraphicsBitmap("/res/Tower/IdleAnimation/tower_00.png"),
      new GraphicsBitmap("/res/Tower/IdleAnimation/tower_03.png"),
      new GraphicsBitmap("/res/Tower/IdleAnimation/tower_06.png"),
      new GraphicsBitmap("/res/Tower/IdleAnimation/tower_09.png"),
      new GraphicsBitmap("/res/Tower/IdleAnimation/tower_11.png")
    )
  }


}