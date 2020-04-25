package com.jqorz.planewar.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.entity.Bullet;
import com.jqorz.planewar.tools.DeviceTools;

/**
 * @author j1997
 * @since 2020/4/7
 */
public abstract class BaseEntityImp implements IEntity {
    protected String TAG = getClass().getSimpleName();
    protected int x;
    protected int y;
    protected Bitmap[] mBitmaps;

    public BaseEntityImp(Bitmap defaultBitmap) {
        this(defaultBitmap, -1);
    }

    public BaseEntityImp(Bitmap[] bitmaps) {
        this(bitmaps, -1);
    }

    public BaseEntityImp(Bitmap defaultBitmap, int type) {
        this(new Bitmap[]{defaultBitmap}, type);
    }

    public BaseEntityImp(Bitmap[] bitmaps, int type) {
        this.mBitmaps = bitmaps;
        init(type);
    }

    public abstract void init(int type);

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


    public boolean isOutOfBound() {
        if (this instanceof Bullet) {
            return isOutOfBounds(false);
        } else {
            return isOutOfBounds(true);
        }
    }

    public boolean isOutOfBounds(boolean isToBottom) {
        if (isToBottom) {
            return getY() > DeviceTools.getScreenHeight() + getHeight();
        } else {
            return getY() < -getHeight();
        }
    }

    @Override
    public String toString() {
        return "BaseEntityImp{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
