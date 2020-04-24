package com.jqorz.planewar.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jqorz.planewar.base.BasePlane;
import com.jqorz.planewar.eenum.PlaneStatus;
import com.jqorz.planewar.eenum.PlaneType;
import com.jqorz.planewar.tools.AnimationImpl;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为敌机类
 */
public class EnemyPlane extends BasePlane {

    private int type;//敌机的类型
    private int velocity;//飞机的速度

    public EnemyPlane(@PlaneType int type) {
        super(type == PlaneType.ENEMY_TYPE1 ? BitmapLoader.bmps_enemyPlane1 : type == PlaneType.ENEMY_TYPE2 ? BitmapLoader.bmps_enemyPlane2 : BitmapLoader.bmps_enemyPlane3);
        setType(type);
    }

    @Override
    public void init() {
        initLife();
        initAnimation();
        reset();
    }

    private void initAnimation() {
        mFlyAnimation = new AnimationImpl(getBitmaps(), true);
        switch (type) {
            case PlaneType.ENEMY_TYPE1:
                mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_enemyPlane1_explode, false);
                mInjureAnimation = new AnimationImpl(getBitmaps(), false);
                break;
            case PlaneType.ENEMY_TYPE2:
                mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_enemyPlane2_explode, false);
                mInjureAnimation = new AnimationImpl(BitmapLoader.bmp_enemyPlane2_injured, false);
                break;
            case PlaneType.ENEMY_TYPE3:
                mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_enemyPlane3_explode, false);
                mInjureAnimation = new AnimationImpl(BitmapLoader.bmp_enemyPlane3_injured, false);
                break;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        switch (status) {
            case PlaneStatus.STATUS_FLY:
                mFlyAnimation.drawAnimation(canvas, paint, x, y);
                break;
            case PlaneStatus.STATUS_EXPLORE:
                mExploreAnimation.drawAnimation(canvas, paint, x, y);
                break;
            case PlaneStatus.STATUS_INJURE:
                mInjureAnimation.drawAnimation(canvas, paint, x, y);
                break;
        }
    }

    public void reset() {
        setX(0);
        setY(-getHeight());
        setStatus(PlaneStatus.STATUS_FLY);
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    protected void initLife() {
        switch (type) {
            case PlaneType.ENEMY_TYPE1:
                this.life = ConstantUtil.Enemy1_life;
                break;
            case PlaneType.ENEMY_TYPE2:
                this.life = ConstantUtil.Enemy2_life;
                break;
            case PlaneType.ENEMY_TYPE3:
                this.life = ConstantUtil.Enemy3_life;
                break;
        }
    }


    public void move() {
        this.y = this.y + velocity;
    }


}