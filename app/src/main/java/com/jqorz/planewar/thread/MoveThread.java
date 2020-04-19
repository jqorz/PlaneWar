package com.jqorz.planewar.thread;

import com.jqorz.planewar.entity.BombSupply;
import com.jqorz.planewar.entity.Bullet;
import com.jqorz.planewar.entity.ChangeBullet;
import com.jqorz.planewar.entity.EnemyPlane;
import com.jqorz.planewar.entity.GameView;
import com.jqorz.planewar.utils.ConstantUtil;

import java.util.ArrayList;

/**
 * 除了我方飞机外所有移动物的移动线程
 */

public class MoveThread extends Thread {
    GameView gameView;
    private boolean flag = true;//循环标志位
    private ArrayList<Bullet> deleteBullets = new ArrayList<>();

    public MoveThread(GameView gameView) {//构造器
        this.gameView = gameView;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void run() {
        while (flag) {

            //子弹碰撞检测
            try {
                for (Bullet b : gameView.mBullets) {
                    if (b.getY() < -b.getBitmap().getHeight()) {
                        deleteBullets.add(b);
                    } else {
                        for (EnemyPlane ep : gameView.mEnemy) {
                            if (ep.isLive() && ep.getBitmap() != null) {
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
                } else if (ep.isLive() && gameView.heroPlane.isLive() && gameView.heroPlane.getBitmap() != null) {
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
            } else if (b.getStatus() && gameView.heroPlane.isLive() && gameView.heroPlane.getBitmap() != null) {
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
            } else if (cb.getStatus() && gameView.heroPlane.isLive() && gameView.heroPlane.getBitmap() != null) {
                if (gameView.heroPlane.contain(cb)) {
                    cb.setStatus(false);
                    cb.reset();
                }


            }


        }
        try {
            Thread.sleep(ConstantUtil.MOVE_THREAD_SPAN);
        } catch (InterruptedException e) {

        }
    }
}