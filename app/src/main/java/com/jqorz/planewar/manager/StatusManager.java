package com.jqorz.planewar.manager;

import com.jqorz.planewar.constant.BulletType;
import com.jqorz.planewar.constant.PlaneStatus;
import com.jqorz.planewar.entity.BgEntity;
import com.jqorz.planewar.entity.BombSupply;
import com.jqorz.planewar.entity.Bullet;
import com.jqorz.planewar.entity.BulletSupply;
import com.jqorz.planewar.entity.EnemyPlane;
import com.jqorz.planewar.entity.GameView;
import com.jqorz.planewar.entity.HeroPlane;
import com.jqorz.planewar.tools.CollisionCheck;
import com.jqorz.planewar.tools.DeviceTools;
import com.jqorz.planewar.tools.MapCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author j1997
 * @since 2020/4/12
 */
public class StatusManager implements TimeManager.OnEntityChangeListener {
    public boolean showEnemy = false;
    public boolean showHero = false;
    public boolean useBomb = false;
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
            if (enemyPlane.isHide()) {
                continue;
            }
            if (useBomb) {
                if (enemyPlane.isFly() || enemyPlane.isInjure()) {
                    enemyPlane.setStatus(PlaneStatus.STATUS_INJURE);
                    enemyPlane.setLife(0);
                }
            }


            if (enemyPlane.isFly()) {
                for (Bullet bullet : gameView.mBullets) {
                    if (CollisionCheck.isCollision(enemyPlane, bullet)) {//打中敌机
                        enemyPlane.setStatus(PlaneStatus.STATUS_INJURE);
                        enemyPlane.setLife(enemyPlane.getLife() - 1);//生命减1
                        bullet.setShown(false);
                        gameView.onEnemyAttacked(enemyPlane.getType());
                    }
                }
                if (CollisionCheck.isCollision(heroPlane, enemyPlane)) {
                    enemyPlane.setStatus(PlaneStatus.STATUS_INJURE);
                    enemyPlane.setLife(0);//生命减1
                    heroPlane.setStatus(PlaneStatus.STATUS_INJURE);
                    heroPlane.setLife(0);
                    gameView.onHeroAttacked();
                }
            } else if (enemyPlane.isInjure()) {
                if (enemyPlane.getLife() <= 0) {//当生命小于0时，向主线程发送得分信息
                    gameView.onEnemyDie(enemyPlane.getType());
                    enemyPlane.setStatus(PlaneStatus.STATUS_EXPLORE);
                    gameView.onEnemyDie(enemyPlane.getType());
                } else {
                    enemyPlane.setStatus(PlaneStatus.STATUS_FLY);
                    enemyPlane.resetInjureAnim();
                }
            } else if (enemyPlane.isExploreEnd()) {
                enemyPlane.setStatus(PlaneStatus.STATUS_HIDE);
            }
        }
        useBomb = false;
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

        //检查英雄飞机状态
        if (heroPlane.isInjure()) {
            if (heroPlane.getLife() <= 0) {//当生命小于0时
                heroPlane.setStatus(PlaneStatus.STATUS_EXPLORE);
                gameView.onHeroDie();
            } else {
                heroPlane.setStatus(PlaneStatus.STATUS_FLY);
                heroPlane.resetInjureAnim();
            }
        } else if (heroPlane.isExploreEnd()) {
            gameView.onGameOver();
        }

        //检查炸弹补给
        if (bombSupply.isShown() && !gameView.heroPlane.isHide()) {
            if (CollisionCheck.isCollision(heroPlane, bombSupply)) {
                bombSupply.setShown(false);
                gameView.onGetBomb(1);
            }
        }


        //检查子弹补给
        if (bulletSupply.isShown() && !gameView.heroPlane.isHide()) {
            if (CollisionCheck.isCollision(heroPlane, bulletSupply)) {
                mTimeManager.onGetBulletSupply();
                heroPlane.setBulletType(BulletType.BULLET_BLUE);
                bulletSupply.setShown(false);
            }
        }

        //检查背景
        for (int i = 0; i < gameView.mBgEntityArray.length; i++) {
            BgEntity bgEntity = gameView.mBgEntityArray[i];
            if (bgEntity.getY() > DeviceTools.getScreenHeight()) {
                //如果是第0个，应该把它排到第length-1后面，否则应该排到i-1后面
                int lastPos = i == 0 ? gameView.mBgEntityArray.length - 1 : i - 1;
                bgEntity.setY(gameView.mBgEntityArray[lastPos].getY() - bgEntity.getHeight());
            }
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
        gameView.mBulletSupply.reset();
    }

    @Override
    public void onShowBombSupply() {
        gameView.mBombSupply.setShown(true);
        gameView.mBombSupply.reset();
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
