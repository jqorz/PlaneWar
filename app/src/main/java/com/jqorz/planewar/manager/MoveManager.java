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
        for (Bullet b : gameView.mBullets) {
            b.move();
        }

        //移动敌军飞机
        for (EnemyPlane ep : gameView.mEnemyPlanes) {
            ep.move();
        }

        //移动炸弹补给
        BombSupply bombSupply = gameView.mBombSupply;
        bombSupply.move();

        //移动子弹补给
        BulletSupply bulletSupply = gameView.mBulletSupply;
        bulletSupply.move();

        //移动背景
        BgEntity bgEntity1 = gameView.mBgEntity1;
        bgEntity1.move();
        BgEntity bgEntity2 = gameView.mBgEntity2;
        bgEntity2.move();


    }
}
