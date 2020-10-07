package com.jqorz.planewar.entity

import android.graphics.Canvas
import android.graphics.Paint
import com.jqorz.planewar.base.BaseEntityImp
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.tools.AnimationImpl
import com.jqorz.planewar.tools.BitmapLoader

/**
 * @author j1997
 * @since 2020/4/19
 */
class BgEntity : BaseEntityImp(arrayOf(BitmapLoader.background)) {
    private var mAnimation = AnimationImpl(bitmaps, true)


    override fun draw(canvas: Canvas, paint: Paint) {
        mAnimation.drawAnimation(canvas, paint, x, y)
    }

    fun move() {
        y += ConstantValue.Bg_Velocity
    }
}