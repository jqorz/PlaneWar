package com.jqorz.planewar.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为爆炸类
 * 在制定位置绘制爆炸
 * 其他线程通过调用nextFrame方法换帧
 */

public class Explode {
    private int x;//爆炸的x坐标
    private int y;//爆炸的y坐标
    private Bitmap[] bitmaps;//所有爆炸的帧数组
    private int k = 0;//当前帧数
    private Bitmap bitmap;//当前帧
    public Explode(int x, int y, int type) {
        this.x = x;
        this.y = y;
        switch (type) {
            case ConstantUtil.ENEMY_TYPE1:
                this.bitmaps = GameView.bmps_enemyPlane1_explode;
                break;
            case ConstantUtil.ENEMY_TYPE2:
                this.bitmaps = GameView.bmps_enemyPlane2_explode;
                break;
            case ConstantUtil.ENEMY_TYPE3:
                this.bitmaps = GameView.bmps_enemyPlane3_explode;
                break;
            case ConstantUtil.HERO_TYPE:
                this.bitmaps = GameView.bmps_hero_explode;
                break;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void draw(Canvas canvas) {//绘制方法
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, x, y, new Paint());
        }
    }

    public boolean nextFrame() {//换帧，成功返回true。否则返回false
        if (k < bitmaps.length) {
            bitmap = bitmaps[k];
            k++;
            return true;
        }
        return false;
    }
}