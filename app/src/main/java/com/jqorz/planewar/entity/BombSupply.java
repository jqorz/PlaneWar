package com.jqorz.planewar.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为炸弹补给类
 */
public class BombSupply extends BaseSupply {
    private boolean status = false;//炸弹补给的状态


    BombSupply(Bitmap defaultBitmap) {
        super(defaultBitmap);
        reset();
    }

    //重置坐标
    public void reset() {
        setX(Map.getNewSupplyPos(this));
        setY(-getHeight());
    }


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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