package com.jqorz.planewar.tools;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.eenum.PlaneType;
import com.jqorz.planewar.entity.GameView;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

import java.util.Random;

public class MapCreator {

    public static int getNewSupplyPos(BaseSupply supply) {
        return new Random().nextInt(GameView.screenWidth - supply.getWidth());
    }

    public static PlaneInfo getNewEnemyPlaneInfo() {
        Random ra1 = new Random();

        int x;//随机飞机位置
        int velocity;//飞机飞行速度

        double d1 = ra1.nextDouble();
        int type;//概率获得飞机类型
        if (d1 <= 0.1d) {
            type = PlaneType.ENEMY_TYPE3;
        } else if (d1 <= 0.4d) {
            type = PlaneType.ENEMY_TYPE2;
        } else {
            type = PlaneType.ENEMY_TYPE1;
        }

        if (type == PlaneType.ENEMY_TYPE1) {//如果类型为1，在当前速度与最大速度取一个值
            velocity = ra1.nextInt(ConstantUtil.ENEMY_VELOCITY_MAX1 - ConstantUtil.ENEMY_VELOCITY) + ConstantUtil.ENEMY_VELOCITY;
        } else if (type == PlaneType.ENEMY_TYPE2) {//如果类型为2，在当前速度与最大速度取一个值
            velocity = ra1.nextInt(ConstantUtil.ENEMY_VELOCITY_MAX2 - ConstantUtil.ENEMY_VELOCITY) + ConstantUtil.ENEMY_VELOCITY;
        } else {//如果类型为3，取当前速度
            velocity = ConstantUtil.ENEMY_VELOCITY;
        }

        if (type == PlaneType.ENEMY_TYPE1) {
            x = ra1.nextInt(GameView.screenWidth - BitmapLoader.bmps_enemyPlane1[0].getWidth());
        } else if (type == PlaneType.ENEMY_TYPE2) {
            x = ra1.nextInt(GameView.screenWidth - BitmapLoader.bmps_enemyPlane2[0].getWidth());
        } else {
            x = ra1.nextInt(GameView.screenWidth - BitmapLoader.bmps_enemyPlane3[0].getWidth());
        }
        return new PlaneInfo(x, velocity, type);
    }


    public static class PlaneInfo {
        private int x;
        private int velocity;
        private int type;

        public PlaneInfo(int x, int velocity, int type) {
            this.x = x;
            this.velocity = velocity;
            this.type = type;
        }

        public int getX() {
            return x;
        }

        public int getVelocity() {
            return velocity;
        }

        public int getType() {
            return type;
        }
    }

}
