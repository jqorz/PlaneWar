package com.jqorz.planewar.base;

import android.graphics.Bitmap;

/**
 * copyright datedu
 *
 * @author j1997
 * @since 2020/4/7
 */
public abstract class BaseSupply extends BaseEntityImp {

    protected boolean status = false;//补给的状态

    public BaseSupply(Bitmap defaultBitmap) {
        super(defaultBitmap);
        reset();
    }

    public abstract void reset();

    public abstract void move();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
