package com.jqorz.planewar.base;

import android.graphics.Bitmap;

import com.jqorz.planewar.eenum.PlaneStatus;
import com.jqorz.planewar.tools.AnimationImpl;

/**
 * @author j1997
 * @since 2020/4/7
 */
public abstract class BasePlane extends BaseEntityImp {
    protected int status = PlaneStatus.STATUS_HIDE;
    protected int life;//生命
    protected AnimationImpl mFlyAnimation;
    protected AnimationImpl mExploreAnimation;
    protected AnimationImpl mInjureAnimation;

    public BasePlane(Bitmap[] bitmaps) {
        super(bitmaps);
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    protected abstract void initLife();

    public boolean isLive() {
        return status != PlaneStatus.STATUS_HIDE;
    }

    public boolean isFly() {
        return status == PlaneStatus.STATUS_FLY;
    }

    public boolean isHide() {
        return status == PlaneStatus.STATUS_HIDE;
    }

    public boolean isExplore() {
        return status == PlaneStatus.STATUS_EXPLORE;
    }

    public boolean isInjure() {
        return status == PlaneStatus.STATUS_INJURE;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    protected AnimationImpl getStatusAnim(int status) {
        switch (status) {
            case PlaneStatus.STATUS_EXPLORE:
                return mExploreAnimation;
            case PlaneStatus.STATUS_INJURE:
                return mInjureAnimation;
            default:
                return mFlyAnimation;
        }
    }

    public boolean isExploreEnd() {
        return isExplore() && getStatusAnim(PlaneStatus.STATUS_EXPLORE).isEnd;
    }

    public boolean isInjureEnd() {
        return isInjure() ;
    }

    public abstract void move();
}
