package com.jqorz.planewar.base;

import android.graphics.Bitmap;

/**
 *
 * @author j1997
 * @since 2020/4/7
 */
public interface IEntity {

    void setXY(int x,int y);

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    Bitmap[] getBitmaps();
}
