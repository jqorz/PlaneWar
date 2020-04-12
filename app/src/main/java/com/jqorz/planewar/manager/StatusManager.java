package com.jqorz.planewar.manager;

import com.jqorz.planewar.entity.BombSupply;
import com.jqorz.planewar.entity.Bullet;
import com.jqorz.planewar.entity.ChangeBullet;
import com.jqorz.planewar.entity.EnemyPlane;
import com.jqorz.planewar.entity.GameView;

/**
 * copyright datedu
 *
 * @author j1997
 * @since 2020/4/12
 */
public class StatusManager {
    private GameView gameView;

    public StatusManager(GameView gameView) {//构造器
        this.gameView = gameView;
    }

    public void checkStatus() {
        {

            //子弹碰撞检测
            try {
                for (Bullet b : gameView.mBullets) {
                    if (b.getY() < -b.getBitmap().getHeight()) {
                        deleteBullets.add(b);
                    } else {
                        for (EnemyPlane ep : gameView.mEnemy) {
                            if (ep.getStatus() && ep.getBitmap() != null) {
                                if (ep.contain(b, gameView)) {//打中敌机
                                    deleteBullets.add(b);
                                }
                            }
                        }
                    }
                }
                gameView.mBullets.removeAll(deleteBullets);
                deleteBullets.clear();
            } catch (Exception e) {

            }
            //敌军飞机碰撞检测
            for (EnemyPlane ep : gameView.mEnemy) {

                if (ep.getY() > GameView.screenHeight) {
                    ep.setStatus(false);
                } else if (ep.getStatus() && gameView.heroPlane.getStatus() && gameView.heroPlane.getBitmap() != null) {
                    if (gameView.heroPlane.contain(ep, gameView)) {
                        if (ep.getLife() <= 0) {
                            ep.setStatus(false);
                        }
                    }

                }

            }


            //移动炸弹补给
            BombSupply b = gameView.mBombSupply;
            if (b.getY() > GameView.screenHeight) {
                b.setStatus(false);
                b.reset();
            } else if (b.getStatus() && gameView.heroPlane.getStatus() && gameView.heroPlane.getBitmap() != null) {
                if (gameView.heroPlane.contain(b, gameView)) {
                    b.setStatus(false);
                    b.reset();

                }

            }


            //移动子弹补给
            ChangeBullet cb = gameView.changeBullet;

            if (cb.getY() > GameView.screenHeight) {
                cb.setStatus(false);
                cb.reset();
            } else if (cb.getStatus() && gameView.heroPlane.getStatus() && gameView.heroPlane.getBitmap() != null) {
                if (gameView.heroPlane.contain(cb)) {
                    cb.setStatus(false);
                    cb.reset();
                }


            }


        }
    }
}
