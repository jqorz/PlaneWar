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
    protected int mFlyFrameId;
    protected int mExploreFrameId;
    protected int mInjureFrameId;

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

    public boolean isShown() {
        return status != PlaneStatus.STATUS_HIDE;
    }

    public boolean isExplore() {
        return status == PlaneStatus.STATUS_EXPLORE;
    }

    public boolean isInjure() {
        return status == PlaneStatus.STATUS_INJURE;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public abstract void move();
}
