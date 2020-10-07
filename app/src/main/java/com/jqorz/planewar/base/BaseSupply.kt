package com.jqorz.planewar.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.jqorz.planewar.tools.AnimationImpl

/**
 * @author j1997
 * @since 2020/4/7
 */
abstract class BaseSupply(bitmaps: Array<Bitmap>) : BaseEntityImp(bitmaps) {
    override var isHide = true
    protected var mAnimation = AnimationImpl(bitmaps, true)


    override fun draw(canvas: Canvas, paint: Paint) {
        mAnimation.drawAnimation(canvas, paint, x, y)
    }

    abstract fun reset()
    abstract fun move()
}