package com.jqorz.planewar.base

import android.graphics.Bitmap
import com.jqorz.planewar.constant.PlaneStatus
import com.jqorz.planewar.tools.AnimationImpl

/**
 * @author j1997
 * @since 2020/4/7
 */
abstract class BasePlane(bitmaps: Array<Bitmap>) : BaseEntityImp(bitmaps) {
    var currentStatus = PlaneStatus.STATUS_HIDE
    var life = 0//生命
    protected lateinit var mFlyAnimation: AnimationImpl
    protected lateinit var mExploreAnimation: AnimationImpl
    protected lateinit var mInjureAnimation: AnimationImpl

    protected abstract fun initLife()

    fun isFly() = currentStatus == PlaneStatus.STATUS_FLY
    fun isExplore() = currentStatus == PlaneStatus.STATUS_EXPLORE
    fun isInjure() = currentStatus == PlaneStatus.STATUS_INJURE

    override var isHide = currentStatus == PlaneStatus.STATUS_HIDE

    protected fun getStatusAnim(status: PlaneStatus): AnimationImpl {
        return when (status) {
            PlaneStatus.STATUS_EXPLORE -> mExploreAnimation
            PlaneStatus.STATUS_INJURE -> mInjureAnimation
            else -> mFlyAnimation
        }
    }

    open fun isExploreEnd() = isExplore() && getStatusAnim(PlaneStatus.STATUS_EXPLORE).isEnd

    abstract fun move()
}