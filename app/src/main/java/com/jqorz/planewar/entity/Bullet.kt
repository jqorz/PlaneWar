package com.jqorz.planewar.entity

import android.graphics.Canvas
import android.graphics.Paint
import com.jqorz.planewar.base.BaseEntityImp
import com.jqorz.planewar.constant.BulletType
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.tools.AnimationImpl
import com.jqorz.planewar.tools.BitmapLoader

/**
 * 该类为子弹的封装类
 * 记录了子弹自身的相关参数
 * 外界通过调用move方法移动子弹
 */
class Bullet(type: BulletType) : BaseEntityImp(if (type == BulletType.BULLET_RED) BitmapLoader.bmp_bullet1 else BitmapLoader.bmp_bullet2) {
    lateinit var mAnimation: AnimationImpl

    override var isHide = true

    override fun init() {
        mAnimation = AnimationImpl(bitmaps, true)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        mAnimation.drawAnimation(canvas, paint, x, y)
    }

    fun move() {
        y -= ConstantValue.BULLET_VELOCITY
    }
}