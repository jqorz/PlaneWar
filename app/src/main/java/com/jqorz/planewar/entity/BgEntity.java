package com.jqorz.planewar.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.base.BaseEntityImp;
import com.jqorz.planewar.tools.AnimationImpl;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class BgEntity extends BaseEntityImp {
    protected AnimationImpl mAnimation;

    public BgEntity() {
        super(BitmapLoader.background);
    }

    @Override
    public void init(int type) {
        mAnimation = new AnimationImpl(getBitmaps(), true);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        mAnimation.drawAnimation(canvas, paint, x, y);
    }

    public void move() {
        y += ConstantUtil.Bg_Velocity;
    }

}
