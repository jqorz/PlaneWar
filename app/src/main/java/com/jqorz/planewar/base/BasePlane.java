package com.jqorz.planewar.base;

import android.graphics.Bitmap;

/**
 *
 * @author j1997
 * @since 2020/4/7
 */
public abstract class BasePlane extends BaseEntityImp {
    protected boolean status = false;//补给的状态
    protected int life;//生命

    public BasePlane(Bitmap defaultBitmap) {
        super(defaultBitmap);
    }

    public BasePlane(Bitmap[] bitmaps) {
        super(bitmaps);
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public abstract void move();
}
