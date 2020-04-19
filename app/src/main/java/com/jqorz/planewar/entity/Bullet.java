package com.jqorz.planewar.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.tools.AnimationImpl;
import com.jqorz.planewar.base.BaseEntityImp;
import com.jqorz.planewar.eenum.BulletType;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为子弹的封装类
 * 记录了子弹自身的相关参数
 * 外界通过调用move方法移动子弹
 */
public class Bullet extends BaseEntityImp {
    protected AnimationImpl mAnimation;
    protected int mFrameId;
    protected boolean isShown = false;//补给的状态

    public Bullet(@BulletType int type) {
        super(type == BulletType.BULLET_RED ? BitmapLoader.bmp_bullet1 : BitmapLoader.bmp_bullet2);
    }

    @Override
    public void init() {
        mAnimation = new AnimationImpl(getBitmaps(), false);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        mAnimation.drawFrame(canvas, paint, x, y, mFrameId);
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        this.isShown = shown;
    }

    public void move() {
        this.y = this.y - ConstantUtil.BULLET_VELOCITY;
    }

}