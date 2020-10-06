package com.jqorz.planewar.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.jqorz.planewar.entity.Bullet
import com.jqorz.planewar.tools.DeviceTools

/**
 * @author j1997
 * @since 2020/4/7
 */
abstract class BaseEntityImp   constructor(override var bitmaps: Array<Bitmap>, type: Int = -1) : IEntity {
    protected var TAG = javaClass.simpleName
    override var x = 0
    override var y = 0


    constructor(defaultBitmap: Bitmap, type: Int = -1) : this(arrayOf<Bitmap>(defaultBitmap), type) {
    }

    abstract fun init(type: Int)
    abstract fun draw(canvas: Canvas, paint: Paint)
    private val defaultBitmap: Bitmap
        private get() = bitmaps[0]

    override fun setXY(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    val width: Int
        get() = defaultBitmap.width
    val height: Int
        get() = defaultBitmap.height
    val isOutOfBound: Boolean
        get() = (this as? Bullet)?.isOutOfBounds(false) ?: isOutOfBounds(true)

    fun isOutOfBounds(isToBottom: Boolean): Boolean {
        return if (isToBottom) {
            y > DeviceTools.getScreenHeight() + height
        } else {
            y < -height
        }
    }

    override fun toString(): String {
        return "BaseEntityImp{" +
                "x=" + x +
                ", y=" + y +
                '}'
    }

    init {
        init(type)
    }
}