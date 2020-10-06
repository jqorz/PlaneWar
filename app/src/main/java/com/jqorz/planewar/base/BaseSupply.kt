package com.jqorz.planewar.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.jqorz.planewar.tools.AnimationImpl

/**
 * @author j1997
 * @since 2020/4/7
 */
abstract class BaseSupply(defaultBitmap: Bitmap) : BaseEntityImp(defaultBitmap) {
    var isShown = false //补给的状态
    protected var mAnimation: AnimationImpl? = null
    override fun init(type: Int) {
        mAnimation = AnimationImpl(bitmaps, true)
        reset()
    }

    override fun draw(canvas: Canvas , paint: Paint ) {
        mAnimation!!.drawAnimation(canvas, paint, x, y)
    }

    abstract fun reset()
    abstract fun move()
}