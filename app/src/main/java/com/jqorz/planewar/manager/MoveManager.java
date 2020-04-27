package com.jqorz.planewar.manager;

import com.jqorz.planewar.entity.BgEntity;
import com.jqorz.planewar.entity.BombSupply;
import com.jqorz.planewar.entity.Bullet;
import com.jqorz.planewar.entity.BulletSupply;
import com.jqorz.planewar.entity.EnemyPlane;
import com.jqorz.planewar.entity.GameView;

/**
 * @author j1997
 * @since 2020/4/12
 */
public class MoveManager {
    private GameView gameView;

    public MoveManager(GameView gameView) {//构造器
        this.gameView = gameView;
    }

    public void moveEntity() {

        //移动子弹
        for (Bullet bullet : gameView.mBullets) {
            if (bullet.isShown()) {
                bullet.move();
            }
        }

        //移动敌军飞机
        for (EnemyPlane enemyPlane : gameView.mEnemyPlanes) {
            if (!enemyPlane.isHide()) {
                enemyPlane.move();
            }
        }

        //移动炸弹补给
        BombSupply bombSupply = gameView.mBombSupply;
        if (bombSupply.isShown()) {
            bombSupply.move();
        }

        //移动子弹补给
        BulletSupply bulletSupply = gameView.mBulletSupply;
        if (bulletSupply.isShown()) {
            bulletSupply.move();
        }

        //移动背景
        for (BgEntity bgEntity:gameView.mBgEntityArray) {
            bgEntity.move();
        }

    }
}
