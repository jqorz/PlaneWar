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
class EnemyPlane(@PlaneType type: Int) : BasePlane(if (type == PlaneType.ENEMY_TYPE1) BitmapLoader.bmps_enemyPlane1 else if (type == PlaneType.ENEMY_TYPE2) BitmapLoader.bmps_enemyPlane2 else BitmapLoader.bmps_enemyPlane3, type) {
    var type //敌机的类型
            = 0
    private var velocity //飞机的速度
            = 0

    override fun init(type: Int) {
        type = type
        initLife()
        initAnimation()
        reset()
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

    override fun draw(canvas: Canvas, paint: Paint) {
        getStatusAnim(status).drawAnimation(canvas, paint, x, y)
    }

    fun reset() {
        setX(0)
        setY(-height)
        setStatus(PlaneStatus.STATUS_FLY)
    }

    fun resetInjureAnim() {
        getStatusAnim(PlaneStatus.STATUS_INJURE).reset()
    }

    fun setVelocity(velocity: Int) {
        this.velocity = velocity
    }

    override fun initLife() {
        when (type) {
            PlaneType.ENEMY_TYPE1 -> life = ConstantValue.Enemy1_life
            PlaneType.ENEMY_TYPE2 -> life = ConstantValue.Enemy2_life
            PlaneType.ENEMY_TYPE3 -> life = ConstantValue.Enemy3_life
        }
    }

    override fun setStatus(status: Int) {
        val lastStatus = getStatus()
        super.setStatus(status)
        if (status != lastStatus) {
            getStatusAnim(lastStatus).pause()
            getStatusAnim(status).resume()
        }
    }

    override fun move() {
        y = y + velocity
    }

    override fun toString(): String {
        return "EnemyPlane{" +
                "type=" + type +
                ", status=" + status +
                ", life=" + life +
                ", x=" + x +
                '}'
    }
}