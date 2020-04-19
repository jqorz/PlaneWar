package com.jqorz.planewar.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jqorz.planewar.R;
import com.jqorz.planewar.entity.GameView;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class BitmapLoader {
    public static Bitmap[] bmps_enemyPlane1_explode = new Bitmap[4];//敌机1爆炸的数组
    public static Bitmap[] bmps_enemyPlane2_explode = new Bitmap[4];//敌机2爆炸的数组
    public static Bitmap[] bmps_enemyPlane3_explode = new Bitmap[6];//敌机3爆炸的数组
    public static Bitmap[] bmps_hero_explode = new Bitmap[4];//英雄飞机爆炸的数组

    public static Bitmap bmp_enemyPlane2_injured;//敌机2受伤的图片
    public static Bitmap bmp_enemyPlane3_injured;//敌机3受伤的图片

    public static Bitmap[] bmps_enemyPlane1 = new Bitmap[1];//敌机1飞行的数组
    public static Bitmap[] bmps_enemyPlane2 = new Bitmap[1];//敌机2飞行的数组
    public static Bitmap[] bmps_enemyPlane3 = new Bitmap[2];//敌机3飞行的数组
    public static Bitmap[] bmps_heroPlane = new Bitmap[2];//英雄飞机飞行的数组

    public static Bitmap bmp_changeBullet;//子弹补给的图片
    public static Bitmap bmp_Bomb;//炸弹补给的图片
    public static Bitmap bmp_bullet1;
    public static Bitmap bmp_bullet2;

    public static Bitmap background;//背景的大图元


    public static void init(Context context) {
        bmps_heroPlane[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_hero_run1);//英雄飞机的图片1
        bmps_heroPlane[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_hero_run2);//英雄飞机的图片2
        bmps_enemyPlane1[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_enemy1_run);//敌机1的图片
        bmps_enemyPlane2[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_enemy2_run);//敌机2的图片
        bmps_enemyPlane3[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_enemy3_run1);//敌机3的图片1
        bmps_enemyPlane3[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_enemy3_run2);//敌机3的图片2

        bmp_Bomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_supply_bomb);
        bmp_changeBullet = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_supply_bullet);

        bmp_bullet1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_bullet_red);
        bmp_bullet2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_bullet_blue);

        bmp_enemyPlane2_injured = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_enemy2_injured);
        bmp_enemyPlane3_injured = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_enemy3_injured);

        int[] IDs_heroPlane_explode;//英雄飞机爆炸的图片资源ID数组
        int[] IDs_enemyPlane1_explode;//敌机1爆炸的图片资源ID数组
        int[] IDs_enemyPlane2_explode;//敌机2爆炸的图片资源ID数组
        int[] IDs_enemyPlane3_explode;//敌机3爆炸的图片资源ID数组

        IDs_heroPlane_explode = new int[]{//英雄飞机爆炸的所有帧
                R.drawable.playing_hero_dead1,
                R.drawable.playing_hero_dead2,
                R.drawable.playing_hero_dead3,
                R.drawable.playing_hero_dead4,
        };
        IDs_enemyPlane1_explode = new int[]{//敌机1爆炸的所有帧
                R.drawable.playing_enemy1_dead1,
                R.drawable.playing_enemy1_dead2,
                R.drawable.playing_enemy1_dead3,
                R.drawable.playing_enemy1_dead4,
        };
        IDs_enemyPlane2_explode = new int[]{//敌机2爆炸的所有帧
                R.drawable.playing_enemy2_dead1,
                R.drawable.playing_enemy2_dead2,
                R.drawable.playing_enemy2_dead3,
                R.drawable.playing_enemy2_dead4,
        };
        IDs_enemyPlane3_explode = new int[]{//敌机3爆炸的所有帧
                R.drawable.playing_enemy3_dead1,
                R.drawable.playing_enemy3_dead2,
                R.drawable.playing_enemy3_dead3,
                R.drawable.playing_enemy3_dead4,
                R.drawable.playing_enemy3_dead5,
                R.drawable.playing_enemy3_dead6,
        };
        for (int i = 0; i < bmps_hero_explode.length; i++) {//初始化英雄飞机爆炸图片
            bmps_hero_explode[i] = BitmapFactory.decodeResource(context.getResources(), IDs_heroPlane_explode[i]);
        }
        for (int i = 0; i < bmps_enemyPlane1_explode.length; i++) {//初始化敌机1爆炸图片
            bmps_enemyPlane1_explode[i] = BitmapFactory.decodeResource(context.getResources(), IDs_enemyPlane1_explode[i]);
        }
        for (int i = 0; i < bmps_enemyPlane2_explode.length; i++) {//初始化敌机2爆炸图片
            bmps_enemyPlane2_explode[i] = BitmapFactory.decodeResource(context.getResources(), IDs_enemyPlane2_explode[i]);
        }
        for (int i = 0; i < bmps_enemyPlane3_explode.length; i++) {//初始化敌机3爆炸图片
            bmps_enemyPlane3_explode[i] = BitmapFactory.decodeResource(context.getResources(), IDs_enemyPlane3_explode[i]);
        }

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_background);//大背景图片
        background = Bitmap.createScaledBitmap(bmp, GameView.screenWidth, GameView.screenHeight, true);

    }
}
