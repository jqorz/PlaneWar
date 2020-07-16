package com.jqorz.planewar.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.jqorz.planewar.R;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class BitmapLoader {
    public static Bitmap[] bmps_enemyPlane1_explode = new Bitmap[4];//敌机1爆炸的数组
    public static Bitmap[] bmps_enemyPlane2_explode = new Bitmap[4];//敌机2爆炸的数组
    public static Bitmap[] bmps_enemyPlane3_explode = new Bitmap[7];//敌机3爆炸的数组
    public static Bitmap[] bmps_hero_explode = new Bitmap[4];//英雄飞机爆炸的数组

    public static Bitmap bmp_enemyPlane2_injured;//敌机2受伤的图片
    public static Bitmap bmp_enemyPlane3_injured;//敌机3受伤的图片

    public static Bitmap[] bmps_enemyPlane1 = new Bitmap[1];//敌机1飞行的数组
    public static Bitmap[] bmps_enemyPlane2 = new Bitmap[1];//敌机2飞行的数组
    public static Bitmap[] bmps_enemyPlane3 = new Bitmap[2];//敌机3飞行的数组
    public static Bitmap[] bmps_heroPlane = new Bitmap[2];//英雄飞机飞行的数组

    public static Bitmap bmp_bulletSupply;//子弹补给的图片
    public static Bitmap bmp_bombSupply;//炸弹补给的图片
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

        bmp_bombSupply = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_supply_bomb);
        bmp_bulletSupply = BitmapFactory.decodeResource(context.getResources(), R.drawable.playing_supply_bullet);

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
        int toWidth = DeviceTools.getScreenWidth();
        int toHeight = DeviceTools.getScreenHeight();
        if (bmp.getWidth() != 0) {
            toHeight = toWidth * bmp.getHeight() / bmp.getWidth();
        }
        //按照背景图的宽高比，将背景图的宽度缩放至屏幕宽度，高度等比缩放
        background = Bitmap.createScaledBitmap(bmp, toWidth, toHeight, true);
    }

    public static void init2(Context context) {
        try {
            NSDictionary dic = (NSDictionary) PropertyListParser.parse(DeviceTools.getApp().getAssets().open("GameArts.plist"));
            NSDictionary object0 = (NSDictionary) dic.getHashMap().get("frames");
            if (object0 == null) return;
            Bitmap big = AssetsLoader.getBitmapFromAssets(context, "GameArts.png");
            for (Map.Entry<String, NSObject> key0 : object0.getHashMap().entrySet()) {
                BitmapInfo info = new BitmapInfo();
                info.name = key0.getKey();
                NSDictionary object = ((NSDictionary) key0.getValue());
                for (Map.Entry<String, NSObject> key1 : object.getHashMap().entrySet()) {
                    switch (key1.getKey()) {
                        case "spriteSize":
                            info.size = (String) key1.getValue().toJavaObject();
                            continue;
                        case "textureRect":
                            info.location = (String) key1.getValue().toJavaObject();
                            continue;
                    }
                }
                createBitmap(big, info);
            }

        } catch (IOException | PropertyListFormatException | ParseException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }


    }

    private static void createBitmap(Bitmap big, BitmapInfo info) {
        String[] data = info.location.trim().replace("{", "").replace("}", "").split(",");
        int[] location = new int[]{Integer.parseInt(data[0].trim()), Integer.parseInt(data[1].trim()), Integer.parseInt(data[2].trim()), Integer.parseInt(data[3].trim())};
        File file = new File("/sdcard/" + info.name);
        info.name = info.name.substring(0, info.name.lastIndexOf("."));
        saveBitmap(getBitmap(big, location), file);
        switch (info.name) {
            case "background_2":
                background = getBitmap(big, location);
                break;
            case "bomb":
                bmp_bombSupply = getBitmap(big, location);
                break;
            case "bullet1":
                bmp_bullet1 = getBitmap(big, location);
                break;
            case "bullet2":
                bmp_bullet2 = getBitmap(big, location);
                break;
            case "enemy1_blowup_1":
                bmps_enemyPlane1_explode[0] = getBitmap(big, location);
                break;
            case "enemy1_blowup_2":
                bmps_enemyPlane1_explode[1] = getBitmap(big, location);
                break;
            case "enemy1_blowup_3":
                bmps_enemyPlane1_explode[2] = getBitmap(big, location);
                break;
            case "enemy1_blowup_4":
                bmps_enemyPlane1_explode[3] = getBitmap(big, location);
                break;
            case "enemy1_fly_1":
                bmps_enemyPlane1[0] = getBitmap(big, location);
                break;
            case "enemy2_blowup_1":
                bmps_enemyPlane3_explode[0] = getBitmap(big, location);
                break;
            case "enemy2_blowup_2":
                bmps_enemyPlane3_explode[1] = getBitmap(big, location);
                break;
            case "enemy2_blowup_3":
                bmps_enemyPlane3_explode[2] = getBitmap(big, location);
                break;
            case "enemy2_blowup_4":
                bmps_enemyPlane3_explode[3] = getBitmap(big, location);
                break;
            case "enemy2_blowup_5":
                bmps_enemyPlane3_explode[4] = getBitmap(big, location);
                break;
            case "enemy2_blowup_6":
                bmps_enemyPlane3_explode[5] = getBitmap(big, location);
                break;
            case "enemy2_blowup_7":
                bmps_enemyPlane3_explode[6] = getBitmap(big, location);
                break;
            case "enemy2_fly_1":
                bmps_enemyPlane3[0] = getBitmap(big, location);
                break;
            case "enemy2_fly_2":
                bmps_enemyPlane3[1] = getBitmap(big, location);
                break;
            case "enemy2_hit_1":
                bmp_enemyPlane3_injured = getBitmap(big, location);
                break;
            case "enemy3_blowup_1":
                bmps_enemyPlane2_explode[0] = getBitmap(big, location);
                break;
            case "enemy3_blowup_2":
                bmps_enemyPlane2_explode[1] = getBitmap(big, location);
                break;
            case "enemy3_blowup_3":
                bmps_enemyPlane2_explode[2] = getBitmap(big, location);
                break;
            case "enemy3_blowup_4":
                bmps_enemyPlane2_explode[3] = getBitmap(big, location);
                break;
            case "enemy3_fly_1":
                bmps_enemyPlane2[0] = getBitmap(big, location);
                break;
            case "enemy4_fly_1":
                break;
            case "enemy5_fly_1":
                break;
            case "enemy_bullet":
                bmp_bullet1 = getBitmap(big, location);
                break;
            case "game_pause":
                break;
            case "game_pause_hl":
                break;
            case "hero_blowup_1":
                bmps_hero_explode[0] = getBitmap(big, location);
                break;
            case "hero_blowup_2":
                bmps_hero_explode[1] = getBitmap(big, location);
                break;
            case "hero_blowup_3":
                bmps_hero_explode[2] = getBitmap(big, location);
                break;
            case "hero_blowup_4":
                bmps_hero_explode[3] = getBitmap(big, location);
                break;
            case "hero_fly_1":
                bmps_heroPlane[0] = getBitmap(big, location);
                break;
            case "hero_fly_2":
                bmps_heroPlane[1] = getBitmap(big, location);
                break;
            case "loading0":
                break;
            case "loading1":
                break;
            case "loading2":
                break;
            case "loading3":
                break;

        }
    }

    private static Bitmap getBitmap(Bitmap big, int[] location) {
        return Bitmap.createBitmap(big, location[0], location[1], location[2], location[3]);
    }

    private static void initBitmap(Context context) {

    }

    public static class BitmapInfo {
        public String name;
        public String location;
        public String size;
    }

    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null)
            return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
