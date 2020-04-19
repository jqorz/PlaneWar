package com.jqorz.planewar.manager;

import android.os.Message;

import com.jqorz.planewar.eenum.BulletType;
import com.jqorz.planewar.eenum.PlaneStatus;
import com.jqorz.planewar.eenum.PlaneType;
import com.jqorz.planewar.entity.BombSupply;
import com.jqorz.planewar.entity.Bullet;
import com.jqorz.planewar.entity.BulletSupply;
import com.jqorz.planewar.entity.EnemyPlane;
import com.jqorz.planewar.entity.GameView;
import com.jqorz.planewar.entity.HeroPlane;
import com.jqorz.planewar.tools.CollisionCheck;
import com.jqorz.planewar.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author j1997
 * @since 2020/4/12
 */
public class StatusManager {
    private GameView gameView;
    private List<Bullet> deleteBullets = new ArrayList<>();
    private List<EnemyPlane> deleteEnemyPlanes = new ArrayList<>();
    private long lastGetBulletSupplyTime = 0L;

    public StatusManager(GameView gameView) {//构造器
        this.gameView = gameView;
    }

    public void checkStatus() {

        //检查子弹
        for (Bullet bullet : gameView.mBullets) {
            if (bullet.getY() < -bullet.getHeight()) {
                bullet.setShown(false);
            } else {
                for (EnemyPlane enemyPlane : gameView.mEnemyPlanes) {
                    if (enemyPlane.isShown()) {//打中敌机
                        if (CollisionCheck.isCollision(enemyPlane, bullet)) {
                            enemyPlane.setStatus(PlaneStatus.STATUS_INJURE);
                            gameView.playSound(2, 0);//播放受到攻击音效
                            enemyPlane.setLife(enemyPlane.getLife() - 1);//生命减1
                            if (enemyPlane.getLife() <= 0) {//当生命小于0时，向主线程发送得分信息
                                Message msg = gameView.activity.myHandler.obtainMessage();
                                switch (enemyPlane.getType()) {
                                    case PlaneType.ENEMY_TYPE1:
                                        gameView.playSound(2, 0);
                                        msg.arg1 = ConstantUtil.ENEMY_TYPE1_SCORE;
                                        break;
                                    case PlaneType.ENEMY_TYPE2:
                                        gameView.playSound(3, 0);
                                        msg.arg1 = ConstantUtil.ENEMY_TYPE2_SCORE;
                                        break;
                                    case PlaneType.ENEMY_TYPE3:
                                        gameView.playSound(4, 0);
                                        msg.arg1 = ConstantUtil.ENEMY_TYPE3_SCORE;
                                        break;
                                }
                                gameView.activity.myHandler.sendMessage(msg);
                                enemyPlane.setStatus(PlaneStatus.STATUS_EXPLORE);
                            }

                            bullet.setShown(false);

                        }


                    }
                }
            }
            if (!bullet.isShown()) {
                deleteBullets.add(bullet);
            }
        }
        gameView.mBullets.removeAll(deleteBullets);
        deleteBullets.clear();

        //检查敌军飞机
        HeroPlane heroPlane = gameView.heroPlane;
        for (EnemyPlane enemyPlane : gameView.mEnemyPlanes) {

            if (enemyPlane.getY() > GameView.screenHeight) {
                enemyPlane.setStatus(PlaneStatus.STATUS_HIDE);
            } else if (enemyPlane.isShown() && heroPlane.isShown()) {
                if (CollisionCheck.isCollision(heroPlane, enemyPlane)) {
                    if (enemyPlane.getLife() <= 0) {
                        enemyPlane.setStatus(PlaneStatus.STATUS_EXPLORE);
                    }
                    heroPlane.setLife(heroPlane.getLife() - 1);
                    if (heroPlane.getLife() <= 0) {//当生命小于0时
                        heroPlane.setStatus(PlaneStatus.STATUS_EXPLORE);
                        gameView.playSound(4, 0);
                        gameView.activity.myHandler.sendEmptyMessage(ConstantUtil.STATE_END);//向主activity发送Handler消息
                    }
                }
            }
            if (!enemyPlane.isShown()) {
                deleteEnemyPlanes.add(enemyPlane);
            }
        }
        gameView.mEnemyPlanes.removeAll(deleteEnemyPlanes);
        deleteEnemyPlanes.clear();


        //检查炸弹补给
        BombSupply bombSupply = gameView.mBombSupply;
        if (bombSupply.getY() > GameView.screenHeight) {
            bombSupply.setShown(false);
        } else if (bombSupply.getShown() && gameView.heroPlane.isShown()) {
            if (CollisionCheck.isCollision(heroPlane, bombSupply)) {
                bombSupply.setShown(false);
                Message msg = gameView.activity.myHandler.obtainMessage();
                msg.arg2 = 1;
                gameView.activity.myHandler.sendMessage(msg);
            }
        }


        //检查子弹补给
        BulletSupply bulletSupply = gameView.mBulletSupply;

        if (bulletSupply.getY() > GameView.screenHeight) {
            bulletSupply.setShown(false);
        } else if (bulletSupply.getShown() && gameView.heroPlane.isShown()) {

            if (CollisionCheck.isCollision(heroPlane, bulletSupply)) {
                lastGetBulletSupplyTime = System.currentTimeMillis();
                heroPlane.setBulletType(BulletType.BULLET_BLUE);
                bulletSupply.setShown(false);

            }

        }
        if (System.currentTimeMillis() - lastGetBulletSupplyTime > ConstantUtil.SUPPLY_BULLET_LONG_TIME) {
            heroPlane.setBulletType(BulletType.BULLET_RED);
        }

        //检查背景
        if (gameView.mBgEntity1.getY() > GameView.screenHeight) {
            gameView.mBgEntity1.setY(gameView.mBgEntity2.getY() - GameView.screenHeight);
        }
        if (gameView.mBgEntity2.getY() > GameView.screenHeight) {
            gameView.mBgEntity2.setY(gameView.mBgEntity1.getY() - GameView.screenHeight);
        }

    }
}
