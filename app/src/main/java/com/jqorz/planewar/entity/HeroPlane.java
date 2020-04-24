package com.jqorz.planewar.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.base.BasePlane;
import com.jqorz.planewar.eenum.BulletType;
import com.jqorz.planewar.eenum.PlaneStatus;
import com.jqorz.planewar.tools.AnimationImpl;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.tools.DeviceTools;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 英雄飞机实体类
 */
public class HeroPlane extends BasePlane {

    private int bulletType;

    public HeroPlane() {
        super(BitmapLoader.bmps_heroPlane);
        init();
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
        setX((DeviceTools.getScreenWidth() - getWidth()) / 2);
        setY((DeviceTools.getScreenHeight() / 3 * 2 - getHeight() / 2));
        setBulletType(BulletType.BULLET_RED);
        setStatus(PlaneStatus.STATUS_FLY);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        getStatusAnim(status).drawAnimation(canvas, paint, x, y);
    }

    private void initAnimation() {
        mFlyAnimation = new AnimationImpl(getBitmaps(), true);
        mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_hero_explode, false);
        mInjureAnimation = new AnimationImpl(getBitmaps(), true);
    }


    public void setX(int x) {
        this.x = Math.min(Math.max(0, x), DeviceTools.getScreenWidth() - getWidth());
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = Math.min(Math.max(0, y), DeviceTools.getScreenHeight() - getHeight());
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
    public void setStatus(int status) {
        int lastStatus = getStatus();
        super.setStatus(status);
        if (status != lastStatus) {
            getStatusAnim(lastStatus).pause();
            getStatusAnim(status).resume();
        }
    }

    @Override
    protected void initLife() {
        this.life = ConstantUtil.Hero_life;
    }

    @Override
    public void move() {

    }

    public boolean isExploreEnd() {
        return isExplore() && getStatusAnim(PlaneStatus.STATUS_EXPLORE).isEnd;
    }
}


