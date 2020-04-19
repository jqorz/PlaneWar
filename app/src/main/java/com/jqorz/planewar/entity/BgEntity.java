package com.jqorz.planewar.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.anim.AnimationImpl;
import com.jqorz.planewar.base.BaseEntityImp;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class BgEntity extends BaseEntityImp {
    protected AnimationImpl mAnimation;
    private int mFrameId;

    public BgEntity() {
        super(BitmapLoader.background);
    }

    @Override
    public void init() {
        mAnimation = new AnimationImpl(getBitmaps(), false);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        mAnimation.drawFrame(canvas, paint, x, y, mFrameId);
    }

    public void move() {
        y += ConstantUtil.Bg_Velocity;
    }

}
