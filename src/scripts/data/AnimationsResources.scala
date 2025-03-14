package scripts.data

import scripts.tools.BetterGraphicsBitmap

object AnimationsResources {

  val ANIM_SOLDIER_IDLE: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Characters/Soldier/idle/soldierIdle_0.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/idle/soldierIdle_1.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/idle/soldierIdle_2.png")
    )}

  val ANIM_SOLDIER_HURT: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Characters/Soldier/hurt/soldierHurt_0.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/hurt/soldierHurt_1.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/hurt/soldierHurt_2.png")
    )}

  val ANIM_SOLDIER_WALK: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_0.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_2.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_4.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_6.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/walk/soldierWalk_7.png")
    )}

  val ANIM_SOLDIER_ATTACK_1: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Characters/Soldier/attack1/soldierAttack1_0.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/attack1/soldierAttack1_1.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/attack1/soldierAttack1_2.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/attack1/soldierAttack1_3.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/attack1/soldierAttack1_4.png"),
      new BetterGraphicsBitmap("/res/Characters/Soldier/attack1/soldierAttack1_5.png")
    )}


  val ANIM_ORC_IDLE: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_0.png"),
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_1.png"),
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/idle/orcIdle_2.png")
    )}

  val ANIM_ORC_HURT: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/hurt/orcHurt_0.png"),
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/hurt/orcHurt_1.png"),
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/hurt/orcHurt_2.png")
    )}

  val ANIM_ORC_ATTACK_1: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_0.png"),

      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/attack1/orcAttack1_2.png"),
        new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/attack1/orcAttack1_3.png"),
        new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/attack1/orcAttack1_5.png")
    )}

  val ANIM_ORC_WALK: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_0.png"),
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_2.png"),
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_4.png"),
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_6.png"),
      new BetterGraphicsBitmap("/res/Characters/Enemy/Orc/walk/orcWalk_7.png")
    )}

  val ANIM_BASE_HURT: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Base/base_0.png"),
      new BetterGraphicsBitmap("/res/Base/base_1.png"),
      new BetterGraphicsBitmap("/res/Base/base_2.png"),
      new BetterGraphicsBitmap("/res/Base/base_1.png"),
      new BetterGraphicsBitmap("/res/Base/base_2.png"),
      new BetterGraphicsBitmap("/res/Base/base_0.png")
    )
  }

  val ANIM_TOWER_IDLE: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Tower/IdleAnimation/tower_00.png"),
      new BetterGraphicsBitmap("/res/Tower/IdleAnimation/tower_03.png"),
      new BetterGraphicsBitmap("/res/Tower/IdleAnimation/tower_06.png"),
      new BetterGraphicsBitmap("/res/Tower/IdleAnimation/tower_09.png"),
      new BetterGraphicsBitmap("/res/Tower/IdleAnimation/tower_11.png")
    )
  }

  val ANIM_TOWER_BULLET: Array[BetterGraphicsBitmap] = {
    Array(
      new BetterGraphicsBitmap("/res/Tower/Bullet/bullet_0.png"),
      new BetterGraphicsBitmap("/res/Tower/Bullet/bullet_1.png"),
      new BetterGraphicsBitmap("/res/Tower/Bullet/bullet_2.png"),
      new BetterGraphicsBitmap("/res/Tower/Bullet/bullet_3.png")
    )
  }

  val ANIM_UI_BASE_LOW: Array[BetterGraphicsBitmap] = {
    Array(
      Constants.UI_BASE_LIFE_1,
      Constants.UI_BASE_LIFE_1_HIGHLIGHTED,
    )
  }


}