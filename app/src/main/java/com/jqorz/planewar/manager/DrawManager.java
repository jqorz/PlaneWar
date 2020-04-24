package com.jqorz.planewar.manager;

import android.graphics.Canvas;

import com.jqorz.planewar.entity.Bullet;
import com.jqorz.planewar.entity.EnemyPlane;
import com.jqorz.planewar.entity.GameView;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class DrawManager {
    private GameView gameView;

    public DrawManager(GameView gameView) {//构造器
        this.gameView = gameView;
    }

    public void drawEntity(Canvas canvas) {

        //绘制游戏背景
        gameView.mBgEntity1.draw(canvas, gameView.mBgPaint);
        gameView.mBgEntity2.draw(canvas, gameView.mBgPaint);

        //绘制玩家飞机
        if (gameView.heroPlane.isLive()) {
            gameView.heroPlane.draw(canvas, gameView.mHeroPlanePaint);
        }

        //绘制炸弹补给
        if (gameView.mBombSupply.isShown()) {
            gameView.mBombSupply.draw(canvas, gameView.mSupplyPlanePaint);
        }

        //绘制子弹补给
        if (gameView.mBulletSupply.isShown()) {
            gameView.mBulletSupply.draw(canvas, gameView.mSupplyPlanePaint);
        }

        //绘制状态正常的敌军飞机
        for (EnemyPlane enemyPlane : gameView.mEnemyPlanes) {
            if (enemyPlane.isLive()) {
                enemyPlane.draw(canvas, gameView.mEmptyPlanePaint);
            }
        }

        //绘制子弹
        for (Bullet bullet : gameView.mBullets) {
            if (bullet.isShown()) {
                bullet.draw(canvas, gameView.mBulletPlanePaint);
            }
        }


    }
}
