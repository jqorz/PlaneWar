package com.jqorz.planewar.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author j1997
 * @since 2020/4/7
 */
public abstract class BaseEntityImp implements IEntity {

    protected int x;
    protected int y;
    protected Bitmap[] mBitmaps;

    public BaseEntityImp(Bitmap defaultBitmap) {
        this(new Bitmap[]{defaultBitmap});
    }

    public BaseEntityImp(Bitmap[] bitmaps) {
        this.mBitmaps = bitmaps;
    }

    public abstract void draw(Canvas canvas, Paint paint);

    private Bitmap getDefaultBitmap() {
        return mBitmaps[0];
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Bitmap[] getBitmaps() {
        return mBitmaps;
    }

    public int getWidth() {
        return getDefaultBitmap().getWidth();
    }

    public int getHeight() {
        return getDefaultBitmap().getHeight();
    }
}
