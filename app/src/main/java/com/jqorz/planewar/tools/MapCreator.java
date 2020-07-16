package com.jqorz.planewar.tools;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.constant.BulletType;
import com.jqorz.planewar.constant.PlaneType;
import com.jqorz.planewar.entity.HeroPlane;
import com.jqorz.planewar.constant.ConstantValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapCreator {

    public static int getNewSupplyPos(BaseSupply supply) {
        return new Random().nextInt(DeviceTools.getScreenWidth() - supply.getWidth());
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
            velocity = ra1.nextInt(ConstantValue.ENEMY_VELOCITY_MAX1 - ConstantValue.ENEMY_VELOCITY) + ConstantValue.ENEMY_VELOCITY;
        } else if (type == PlaneType.ENEMY_TYPE2) {//如果类型为2，在当前速度与最大速度取一个值
            velocity = ra1.nextInt(ConstantValue.ENEMY_VELOCITY_MAX2 - ConstantValue.ENEMY_VELOCITY) + ConstantValue.ENEMY_VELOCITY;
        } else {//如果类型为3，取当前速度
            velocity = ConstantValue.ENEMY_VELOCITY;
        }

        if (type == PlaneType.ENEMY_TYPE1) {
            x = ra1.nextInt(DeviceTools.getScreenWidth() - BitmapLoader.bmps_enemyPlane1[0].getWidth());
        } else if (type == PlaneType.ENEMY_TYPE2) {
            x = ra1.nextInt(DeviceTools.getScreenWidth() - BitmapLoader.bmps_enemyPlane2[0].getWidth());
        } else {
            x = ra1.nextInt(DeviceTools.getScreenWidth() - BitmapLoader.bmps_enemyPlane3[0].getWidth());
        }
        return new PlaneInfo(x, velocity, type);
    }

    public static List<BulletInfo> getNewBulletPos(HeroPlane heroPlane) {

        List<BulletInfo> infoList = new ArrayList<>();
        int x = heroPlane.getX() + (int) ((heroPlane.getWidth() - BitmapLoader.bmp_bullet1.getWidth()) / 2.0);
        int y = heroPlane.getY() - ConstantValue.BULLET_SPAN / 2;


        if (heroPlane.getBulletType() == BulletType.BULLET_RED) {
            BulletInfo b1 = new BulletInfo(x, y, BulletType.BULLET_RED);
            infoList.add(b1);

        } else if (heroPlane.getBulletType() == BulletType.BULLET_BLUE) {
            BulletInfo b1 = new BulletInfo(x, y - ConstantValue.BULLET_SPAN, BulletType.BULLET_BLUE);
            infoList.add(b1);

            BulletInfo b2 = new BulletInfo(x - ConstantValue.BULLET_SPAN, y, BulletType.BULLET_BLUE);
            infoList.add(b2);

            BulletInfo b3 = new BulletInfo(x + ConstantValue.BULLET_SPAN, y, BulletType.BULLET_BLUE);
            infoList.add(b3);
        }
        return infoList;
    }

    public static class BulletInfo {
        private int x;
        private int y;
        private int type;

        public BulletInfo(int x, int y, @BulletType int type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getType() {
            return type;
        }
    }

    public static class PlaneInfo {
        private int x;
        private int velocity;
        private int type;

        public PlaneInfo(int x, int velocity, @PlaneType int type) {
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
