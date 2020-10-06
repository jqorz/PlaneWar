package com.jqorz.planewar.base

import android.graphics.Bitmap

/**
 *
 * @author j1997
 * @since 2020/4/7
 */
interface IEntity {
    fun setXY(x: Int, y: Int)
    var x: Int
    var y: Int
    val bitmaps: Array<Bitmap>
}