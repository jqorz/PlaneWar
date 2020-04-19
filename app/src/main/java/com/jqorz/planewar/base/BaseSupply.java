package com.jqorz.planewar.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.anim.AnimationImpl;

/**
 * @author j1997
 * @since 2020/4/7
 */
public abstract class BaseSupply extends BaseEntityImp {

    protected boolean status = false;//补给的状态
    protected AnimationImpl mAnimation;
    protected int mFrameId;

    public BaseSupply(Bitmap defaultBitmap) {
        super(defaultBitmap);
    }

    @Override
    public void init() {
        mAnimation = new AnimationImpl(getBitmaps(), false);
        mFrameId = 0;
        reset();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        mAnimation.drawFrame(canvas, paint, x, y, mFrameId);
    }

    public abstract void reset();

    public abstract void move();

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
