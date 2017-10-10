package com.jqorz.planewar.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为炸弹补给类
 */
public class Bomb {
    private int x;//炸弹的坐标
    private int y = 0;
    private boolean status = false;//炸弹补给的状态
    private Bitmap bitmap;

    public Bomb(int x) {
        this.bitmap = GameView.bmp_Bomb;
        setX(x);
        y = -bitmap.getHeight();
    }

    //重置坐标
    public void reset() {
        this.setX(Map.getBomb());
        this.y = -bitmap.getHeight();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, x, y, new Paint());
        }
    }

    public void move() {
        this.y = this.y + ConstantUtil.Bomb_Velocity;
    }
}                                                                                                 