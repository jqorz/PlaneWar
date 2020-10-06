package com.jqorz.planewar.entity

import android.graphics.Canvas
import android.graphics.Paint
import com.jqorz.planewar.base.BasePlane
import com.jqorz.planewar.constant.BulletType
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.constant.PlaneStatus
import com.jqorz.planewar.tools.AnimationImpl
import com.jqorz.planewar.tools.BitmapLoader
import com.jqorz.planewar.tools.DeviceTools

/**
 * 英雄飞机实体类
 */
class HeroPlane : BasePlane(BitmapLoader.bmps_heroPlane) {
    lateinit var bulletType: BulletType
    override fun init() {
        initLife()
        initAnimation()
        reset()
    }

    private fun reset() {
        x = ((DeviceTools.getScreenWidth() - width) / 2)
        y = (DeviceTools.getScreenHeight() / 3 * 2 - height / 2)
        bulletType = BulletType.BULLET_RED
        setStatus(PlaneStatus.STATUS_FLY)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        getStatusAnim(currentStatus).drawAnimation(canvas, paint, x, y)
    }

    private fun initAnimation() {
        mFlyAnimation = AnimationImpl(bitmaps, true)
        mExploreAnimation = AnimationImpl(BitmapLoader.bmps_hero_explode, false)
        mInjureAnimation = AnimationImpl(bitmaps, false)
    }

    override var x: Int = 0
        get() = super.x
        set(value) {
            field = Math.min(Math.max(0, value), DeviceTools.getScreenWidth() - width)
        }

    override var y: Int = 0
        get() = super.y
        set(value) {
            field = Math.min(Math.max(0, value), DeviceTools.getScreenHeight() - height)
        }


    fun resetInjureAnim() {
        getStatusAnim(PlaneStatus.STATUS_INJURE).reset()
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

    fun setStatus(status: PlaneStatus) {
        val lastStatus = currentStatus
        super.currentStatus = status
        if (status != lastStatus) {
            getStatusAnim(lastStatus).pause()
            getStatusAnim(status).resume()
        }
    }

    override fun initLife() {
        life = ConstantValue.Hero_life
    }

    override fun move() {}

}