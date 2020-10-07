package com.jqorz.planewar.entity

import android.graphics.Canvas
import android.graphics.Paint
import com.jqorz.planewar.base.BasePlane
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.constant.PlaneStatus
import com.jqorz.planewar.constant.PlaneType
import com.jqorz.planewar.tools.AnimationImpl
import com.jqorz.planewar.tools.BitmapLoader

/**
 * 该类为敌机类
 */
class EnemyPlane(val type: PlaneType) : BasePlane(if (type == PlaneType.ENEMY_TYPE1) BitmapLoader.bmps_enemyPlane1 else if (type == PlaneType.ENEMY_TYPE2) BitmapLoader.bmps_enemyPlane2 else BitmapLoader.bmps_enemyPlane3) {
    private var velocity = 0//飞机的速度

    init {
        initLife()
        initAnimation()
        reset()
    }

    override fun initLife() {
        life = when (type) {
            PlaneType.ENEMY_TYPE1 -> ConstantValue.Enemy1_life
            PlaneType.ENEMY_TYPE2 -> ConstantValue.Enemy2_life
            PlaneType.ENEMY_TYPE3 -> ConstantValue.Enemy3_life
        }
    }

    private fun initAnimation() {
        mFlyAnimation = AnimationImpl(bitmaps, true)
        when (type) {
            PlaneType.ENEMY_TYPE1 -> {
                mExploreAnimation = AnimationImpl(BitmapLoader.bmps_enemyPlane1_explode, false)
                mInjureAnimation = AnimationImpl(bitmaps, false)
            }
            PlaneType.ENEMY_TYPE2 -> {
                mExploreAnimation = AnimationImpl(BitmapLoader.bmps_enemyPlane2_explode, false)
                mInjureAnimation = AnimationImpl(BitmapLoader.bmp_enemyPlane2_injured, false)
            }
            PlaneType.ENEMY_TYPE3 -> {
                mExploreAnimation = AnimationImpl(BitmapLoader.bmps_enemyPlane3_explode, false)
                mInjureAnimation = AnimationImpl(BitmapLoader.bmp_enemyPlane3_injured, false)
            }
        }
    }

    fun reset() {
        x = (0)
        y = (-height)
        currentStatus = PlaneStatus.STATUS_FLY
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        getStatusAnim(currentStatus).drawAnimation(canvas, paint, x, y)
    }

    fun resetInjureAnim() {
        getStatusAnim(PlaneStatus.STATUS_INJURE).reset()
    }

    fun setVelocity(velocity: Int) {
        this.velocity = velocity
    }


    fun setStatus(status: PlaneStatus) {
        val lastStatus = currentStatus
        super.currentStatus = (status)
        if (status != lastStatus) {
            getStatusAnim(lastStatus).pause()
            getStatusAnim(status).resume()
        }
    }

    override fun move() {
        y += velocity
    }

    override fun toString(): String {
        return "EnemyPlane{" +
                "type=" + type +
                ", status=" + currentStatus +
                ", life=" + life +
                ", x=" + x +
                '}'
    }
}