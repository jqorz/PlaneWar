package com.jqorz.planewar.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.tools.AnimationImpl;

/**
 * @author j1997
 * @since 2020/4/7
 */
public abstract class BaseSupply extends BaseEntityImp {

    protected boolean isShown = false;//补给的状态
    protected AnimationImpl mAnimation;

    public BaseSupply(Bitmap defaultBitmap) {
        super(defaultBitmap);
    }

    @Override
    public void init(int type) {
        mAnimation = new AnimationImpl(getBitmaps(), true);
        reset();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        mAnimation.drawAnimation(canvas, paint, x, y);
    }

    public abstract void reset();

    public abstract void move();

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        this.isShown = shown;
    }
}
