package com.jqorz.planewar.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为子弹的封装类
 * 记录了子弹自身的相关参数
 * 外界通过调用move方法移动子弹
 */
public class Bullet {
    private Bitmap bitmap;
    private int x;
    private int y;//子弹的坐标

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        setType(ConstantUtil.BULLET_RED);
    }


    public void setType(int type) {
        switch (type) {
            case ConstantUtil.BULLET_RED:
                bitmap = GameView.bmp_bullet1;
                break;
            case ConstantUtil.BULLET_BLUE:
                bitmap = GameView.bmp_bullet2;
                break;
        }
    }


    public Bitmap getBitmap() {
        return bitmap;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, x, y, new Paint());
        }
    }

    public void move() {
        this.y = this.y - ConstantUtil.BULLET_VELOCITY;
    }
}