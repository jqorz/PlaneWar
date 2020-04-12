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

    public ChangeBullet(Bitmap defaultBitmap) {
        super(defaultBitmap);
    }

    //重置坐标
    @Override
    public void reset() {
        setX(Map.getNewSupplyPos(this));
        setY(-getHeight());
    }


    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, x, y, new Paint());
        }
    }

    public void move() {
        y += ConstantUtil.ChangeBullet_Velocity;
    }
}