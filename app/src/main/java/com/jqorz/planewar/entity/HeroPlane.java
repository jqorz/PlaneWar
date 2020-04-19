package com.jqorz.planewar.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.base.BasePlane;
import com.jqorz.planewar.eenum.BulletType;
import com.jqorz.planewar.eenum.PlaneStatus;
import com.jqorz.planewar.tools.AnimationImpl;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 英雄飞机实体类
 */
public class HeroPlane extends BasePlane {

    private int bulletType;

    public HeroPlane() {
        super(BitmapLoader.bmps_heroPlane);
    }

    public int getBulletType() {
        return bulletType;
    }

    public void setBulletType(int bulletType) {
        this.bulletType = bulletType;
    }


    @Override
    public void init() {
        initLife();
        initAnimation();
        reset();
    }

    private void reset() {
        setBulletType(BulletType.BULLET_RED);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        switch (status) {
            case PlaneStatus.STATUS_FLY:
                mFlyAnimation.drawFrame(canvas, paint, x, y, mFlyFrameId);
                break;
            case PlaneStatus.STATUS_EXPLORE:
                mExploreAnimation.drawFrame(canvas, paint, x, y, mExploreFrameId);
                break;
            case PlaneStatus.STATUS_INJURE:
                mInjureAnimation.drawFrame(canvas, paint, x, y, mInjureFrameId);
                break;
        }
    }

    private void initAnimation() {
        mFlyAnimation = new AnimationImpl(getBitmaps(), true);
        mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_hero_explode, false);
        mInjureAnimation = new AnimationImpl(getBitmaps(), false);
    }


    public void setX(int x) {
        this.x = Math.min(Math.max(0, x), GameView.screenWidth - getWidth());
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = Math.min(Math.max(0, y), GameView.screenHeight - getHeight());
    }


/*

    public void drawLife(Canvas canvas) {
        if (bmp_life != null) {
            for (int j = 0; j < ((5 - Hp) < 0 ? 5 : Hp); j++) {//绘制表示生命的小数条
                canvas.drawBitmap(bmp_life, 45 + bmp_life.getWidth() * j, 13, new Paint());
            }
        }
    }
*/

    @Override
    protected void initLife() {
        this.life = ConstantUtil.Hero_life;
    }

    @Override
    public void move() {

    }
}


