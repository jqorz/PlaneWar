package com.jqorz.planewar.base;

import android.graphics.Bitmap;

import com.jqorz.planewar.anim.AnimationImpl;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * @author j1997
 * @since 2020/4/7
 */
public abstract class BasePlane extends BaseEntityImp {
    protected int status = ConstantUtil.STATUS_HIDE;
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

    public abstract int getLife();

    protected abstract void initLife();

    public boolean isLive() {
        return status == ConstantUtil.STATUS_FLY;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public abstract void move();
}
