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
public class StatusManager implements TimeManager.OnEntityChangeListener {
    public boolean showEnemy = false;
    public boolean showHero = false;
    private GameView gameView;
    private List<Bullet> deleteBullets = new ArrayList<>();
    private List<EnemyPlane> deleteEnemyPlanes = new ArrayList<>();
    private TimeManager mTimeManager;

    public StatusManager(GameView gameView) {//构造器
        this.gameView = gameView;
        mTimeManager = new TimeManager(gameView, this);
        mTimeManager.initTime();
        mTimeManager.setOnEntityChangeListener(this);
    }

    public void checkStatus() {

        mTimeManager.checkNew();

        //检查越界
        for (Bullet bullet : gameView.mBullets) {
            if (bullet.isOutOfBound()) {
                bullet.setShown(false);
            }
        }
        for (EnemyPlane enemyPlane : gameView.mEnemyPlanes) {
            if (enemyPlane.isOutOfBound()) {
                enemyPlane.setStatus(PlaneStatus.STATUS_HIDE);
            }
        }
        BombSupply bombSupply = gameView.mBombSupply;
        if (bombSupply.isOutOfBound()) {
            bombSupply.setShown(false);
        }
        BulletSupply bulletSupply = gameView.mBulletSupply;
        if (bulletSupply.isOutOfBound()) {
            bulletSupply.setShown(false);
        }

        HeroPlane heroPlane = gameView.heroPlane;

        //检查敌军飞机
        for (EnemyPlane enemyPlane : gameView.mEnemyPlanes) {
            if (enemyPlane.isLive()) {
                if (heroPlane.isLive()) {
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
                if (enemyPlane.isFly()) {
                    for (Bullet bullet : gameView.mBullets) {
                        if (CollisionCheck.isCollision(enemyPlane, bullet)) {//打中敌机
                            enemyPlane.setStatus(PlaneStatus.STATUS_INJURE);
                            gameView.playSound(2, 0);//播放受到攻击音效
                            enemyPlane.setLife(enemyPlane.getLife() - 1);//生命减1
                            bullet.setShown(false);
                            break;
                        }
                    }
                } else if (enemyPlane.isInjureEnd()) {
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
                    } else {
                        enemyPlane.setStatus(PlaneStatus.STATUS_FLY);
                    }
                } else if (enemyPlane.isExploreEnd()) {
                    enemyPlane.setStatus(PlaneStatus.STATUS_HIDE);
                }


            }
        }

        for (EnemyPlane enemyPlane1 : gameView.mEnemyPlanes) {
            if (enemyPlane1.isHide()) {
                deleteEnemyPlanes.add(enemyPlane1);
            }
        }
        gameView.mEnemyPlanes.removeAll(deleteEnemyPlanes);
        deleteEnemyPlanes.clear();

        for (Bullet bullet : gameView.mBullets) {
            if (!bullet.isShown()) {
                deleteBullets.add(bullet);
            }
        }
        gameView.mBullets.removeAll(deleteBullets);
        deleteBullets.clear();

        //检查炸弹补给
        if (bombSupply.isShown() && gameView.heroPlane.isLive()) {
            if (CollisionCheck.isCollision(heroPlane, bombSupply)) {
                bombSupply.setShown(false);
                Message msg = gameView.activity.myHandler.obtainMessage();
                msg.arg2 = 1;
                gameView.activity.myHandler.sendMessage(msg);
            }
        }


        //检查子弹补给
        if (bulletSupply.isShown() && gameView.heroPlane.isLive()) {
            if (CollisionCheck.isCollision(heroPlane, bulletSupply)) {
                mTimeManager.onGetBulletSupply();
                heroPlane.setBulletType(BulletType.BULLET_BLUE);
                bulletSupply.setShown(false);
            }
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
        enemyPlane.setStatus(PlaneStatus.STATUS_FLY);
        enemyPlane.setX(planeInfo.getX());
        gameView.mEnemyPlanes.add(enemyPlane);
    }

    @Override
    public void onNewBullet(MapCreator.BulletInfo bulletInfo) {
        Bullet bullet = new Bullet(bulletInfo.getType());
        bullet.setXY(bulletInfo.getX(), bulletInfo.getY());
        bullet.setShown(true);
        gameView.mBullets.add(bullet);
    }

    @Override
    public void onShowBulletSupply() {
        gameView.mBulletSupply.setShown(true);
    }

    @Override
    public void onShowBombSupply() {
        gameView.mBombSupply.setShown(true);
    }

    @Override
    public void onFirstShowEnemy() {
        showEnemy = true;
    }

    @Override
    public void onFirstShowHero() {
        showHero = true;
        gameView.heroPlane.setStatus(PlaneStatus.STATUS_FLY);
    }

    @Override
    public void onBulletSupplyEnd() {
        gameView.heroPlane.setBulletType(BulletType.BULLET_RED);
    }
}
