package com.jqorz.planewar.base

import android.graphics.Bitmap
import com.jqorz.planewar.constant.PlaneStatus
import com.jqorz.planewar.tools.AnimationImpl

/**
 * @author j1997
 * @since 2020/4/7
 */
abstract class BasePlane : BaseEntityImp {
    open var status = PlaneStatus.STATUS_HIDE
    var life //生命
            = 0
    protected var mFlyAnimation: AnimationImpl? = null
    protected var mExploreAnimation: AnimationImpl? = null
    protected var mInjureAnimation: AnimationImpl? = null

    constructor(bitmaps: Array<Bitmap>) : super(bitmaps) {}
    constructor(bitmaps: Array<Bitmap>, type: Int) : super(bitmaps, type) {}

    protected abstract fun initLife()
    val isFly: Boolean
        get() = status == PlaneStatus.STATUS_FLY
    val isHide: Boolean
        get() = status == PlaneStatus.STATUS_HIDE
    val isExplore: Boolean
        get() = status == PlaneStatus.STATUS_EXPLORE
    val isInjure: Boolean
        get() = status == PlaneStatus.STATUS_INJURE

    protected fun getStatusAnim(status: Int): AnimationImpl? {
        return when (status) {
            PlaneStatus.STATUS_EXPLORE -> mExploreAnimation
            PlaneStatus.STATUS_INJURE -> mInjureAnimation
            else -> mFlyAnimation
        }
    }

    open val isExploreEnd: Boolean
        get() = isExplore && getStatusAnim(PlaneStatus.STATUS_EXPLORE)!!.isEnd

    abstract fun move()
}