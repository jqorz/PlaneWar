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
import com.jqorz.planewar.tools.DeviceTools;
import com.jqorz.planewar.tools.MapCreator;
import com.jqorz.planewar.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author j1997
 * @since 2020/4/12
 */
public class StatusManager implements CheckManager.OnEntityChangeListener {
    private GameView gameView;
    private List<Bullet> deleteBullets = new ArrayList<>();
    private List<EnemyPlane> deleteEnemyPlanes = new ArrayList<>();
    private long lastGetBulletSupplyTime = 0L;
    private CheckManager mCheckManager;

    public StatusManager(GameView gameView) {//构造器
        this.gameView = gameView;
        mCheckManager = new CheckManager(gameView);
        mCheckManager.setOnEntityChangeListener(this);
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

            if (enemyPlane.getY() > DeviceTools.getScreenHeight()) {
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
        if (bombSupply.getY() > DeviceTools.getScreenHeight()) {
            bombSupply.setShown(false);
        } else if (bombSupply.isShown() && gameView.heroPlane.isShown()) {
            if (CollisionCheck.isCollision(heroPlane, bombSupply)) {
                bombSupply.setShown(false);
                Message msg = gameView.activity.myHandler.obtainMessage();
                msg.arg2 = 1;
                gameView.activity.myHandler.sendMessage(msg);
            }
        }


        //检查子弹补给
        BulletSupply bulletSupply = gameView.mBulletSupply;

        if (bulletSupply.getY() > DeviceTools.getScreenHeight()) {
            bulletSupply.setShown(false);
        } else if (bulletSupply.isShown() && gameView.heroPlane.isShown()) {

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
        if (gameView.mBgEntity1.getY() > DeviceTools.getScreenHeight()) {
            gameView.mBgEntity1.setY(gameView.mBgEntity2.getY() - DeviceTools.getScreenHeight());
        }
        if (gameView.mBgEntity2.getY() > DeviceTools.getScreenHeight()) {
            gameView.mBgEntity2.setY(gameView.mBgEntity1.getY() - DeviceTools.getScreenHeight());
        }

    }

    @Override
    public void onNewEnemy(MapCreator.PlaneInfo planeInfo) {
        EnemyPlane enemyPlane = new EnemyPlane(planeInfo.getType());
        enemyPlane.setVelocity(planeInfo.getVelocity());
        gameView.mEnemyPlanes.add(enemyPlane);
    }

    @Override
    public void onNewBullet(MapCreator.BulletInfo bulletInfo) {
        Bullet b1 = new Bullet(bulletInfo.getType());
        b1.setXY(bulletInfo.getX(), bulletInfo.getY());
        gameView.mBullets.add(b1);
    }

    @Override
    public void onShowBulletSupply() {
        gameView.mBulletSupply.setShown(true);
    }

    @Override
    public void onShowBombSupply() {
        gameView.mBombSupply.setShown(true);

    }
}
