package com.jqorz.planewar.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为子弹补给类
 */
public class ChangeBullet extends BaseSupply {
    private int x;
    private int y = 0;
    private boolean status = false;
    private Bitmap bitmap;

    public ChangeBullet(Bitmap defaultBitmap) {
        super(defaultBitmap);
        reset();
    }

    //重置坐标
    public void reset() {
        this.setX(Map.getNewSupplyPos(this));
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
        int velocity = ConstantUtil.ChangeBullet_Velocity;
        this.y = this.y + velocity;
    }
}