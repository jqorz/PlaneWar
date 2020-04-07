package com.jqorz.planewar.entity;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.utils.ConstantUtil;

import java.util.HashMap;
import java.util.Random;

 public class Map {

     public static int getNewSupplyPos(BaseSupply supply){
         return new Random().nextInt(GameView.screenWidth - supply.getWidth());
     }


     static HashMap<String, Integer> getEnemyPlan() {//添加敌机
        HashMap<String, Integer> map = new HashMap<>();
        Random ra1 = new Random();

        int x;//随机飞机位置
        int velocity;//飞机飞行速度

        double d1 = ra1.nextDouble();
        int type;//概率获得飞机类型
        if (d1 <= 0.1d) {
            type = ConstantUtil.ENEMY_TYPE3;
        } else if (d1 <= 0.4d) {
            type = ConstantUtil.ENEMY_TYPE2;
        } else {
            type = ConstantUtil.ENEMY_TYPE1;
        }

        if (type == ConstantUtil.ENEMY_TYPE1) {//如果类型为1，在当前速度与最大速度取一个值
            velocity = ra1.nextInt(ConstantUtil.ENEMY_VELOCITY_MAX1 - ConstantUtil.ENEMY_VELOCITY) + ConstantUtil.ENEMY_VELOCITY;
        } else if (type == ConstantUtil.ENEMY_TYPE2) {//如果类型为2，在当前速度与最大速度取一个值
            velocity = ra1.nextInt(ConstantUtil.ENEMY_VELOCITY_MAX2 - ConstantUtil.ENEMY_VELOCITY) + ConstantUtil.ENEMY_VELOCITY;
        } else {//如果类型为3，取当前速度
            velocity = ConstantUtil.ENEMY_VELOCITY;
        }

        if (type == ConstantUtil.ENEMY_TYPE1) {
            x = ra1.nextInt(GameView.screenWidth - GameView.bmps_enemyPlane1[0].getWidth());
        } else if (type == ConstantUtil.ENEMY_TYPE2) {
            x = ra1.nextInt(GameView.screenWidth - GameView.bmps_enemyPlane2[0].getWidth());
        } else {
            x = ra1.nextInt(GameView.screenWidth - GameView.bmps_enemyPlane3[0].getWidth());
        }
        map.put("Location", x);
        map.put("Velocity", velocity);
        map.put("Type", type);

        return map;
    }


}
