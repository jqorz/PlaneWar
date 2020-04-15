package com.jqorz.planewar.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntDef;

import com.jqorz.planewar.anim.AnimationImpl;
import com.jqorz.planewar.base.BaseEntityImp;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为子弹的封装类
 * 记录了子弹自身的相关参数
 * 外界通过调用move方法移动子弹
 */
public class Bullet extends BaseEntityImp {
    protected AnimationImpl mAnimation;
    protected int mFrameId;

    public Bullet(@BulletType int type) {
        this(type == ConstantUtil.BULLET_RED ? GameView.bmp_bullet1 : GameView.bmp_bullet2);
    }

    public Bullet(Bitmap bitmap) {
        super(bitmap);
        mAnimation = new AnimationImpl(getBitmaps(), false);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        mAnimation.drawFrame(canvas, paint, x, y, mFrameId);
    }


    public void move() {
        this.y = this.y - ConstantUtil.BULLET_VELOCITY;
    }

    @IntDef({ConstantUtil.BULLET_RED, ConstantUtil.BULLET_BLUE})
    public @interface BulletType {

    }
}