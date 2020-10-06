package com.jqorz.planewar.manager

import com.jqorz.planewar.entity.GameView

/**
 * @author j1997
 * @since 2020/4/12
 */
class MoveManager     //构造器
(private val gameView: GameView) {
    fun moveEntity() {

        //移动子弹
        for (bullet in gameView.mBullets) {
            if (bullet.isShown) {
                bullet.move()
            }
        }

        //移动敌军飞机
        for (enemyPlane in gameView.mEnemyPlanes) {
            if (!enemyPlane.isHide) {
                enemyPlane.move()
            }
        }

        //移动炸弹补给
        val bombSupply = gameView.mBombSupply
        if (bombSupply.isShown) {
            bombSupply.move()
        }

        //移动子弹补给
        val bulletSupply = gameView.mBulletSupply
        if (bulletSupply!!.isShown) {
            bulletSupply.move()
        }

        //移动背景
        for (bgEntity in gameView.mBgEntityArray) {
            bgEntity.move()
        }
    }
}