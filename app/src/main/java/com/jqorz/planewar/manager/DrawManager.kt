package com.jqorz.planewar.manager

import android.graphics.Canvas
import com.jqorz.planewar.entity.GameView

/**
 * @author j1997
 * @since 2020/4/19
 */
class DrawManager     //构造器
(private val gameView: GameView) {
    fun drawBg(canvas: Canvas?) {
        for (bgEntity in gameView.mBgEntityArray) {
            bgEntity.draw(canvas!!, gameView.mBgPaint)
        }
    }

    fun drawEntity(canvas: Canvas?) {

        //绘制玩家飞机
        if (!gameView.heroPlane.isHide) {
            gameView.heroPlane.draw(canvas!!, gameView.mHeroPlanePaint)
        }

        //绘制炸弹补给
        if (gameView.mBombSupply.isShown) {
            gameView.mBombSupply.draw(canvas!!, gameView.mSupplyPlanePaint)
        }

        //绘制子弹补给
        if (gameView.mBulletSupply!!.isShown) {
            gameView.mBulletSupply!!.draw(canvas!!, gameView.mSupplyPlanePaint)
        }

        //绘制状态正常的敌军飞机
        for (enemyPlane in gameView.mEnemyPlanes) {
            if (!enemyPlane.isHide) {
                enemyPlane.draw(canvas!!, gameView.mEmptyPlanePaint)
            }
        }

        //绘制子弹
        for (bullet in gameView.mBullets) {
            if (bullet.isShown) {
                bullet.draw(canvas!!, gameView.mBulletPlanePaint)
            }
        }
    }
}