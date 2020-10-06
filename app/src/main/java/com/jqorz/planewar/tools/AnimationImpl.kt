package com.jqorz.planewar.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

class AnimationImpl(frameBitmap: Array<Bitmap>, isLoop: Boolean) {
    var isEnd = false
    var isPause = false
    private var mPlayID = 0
    private val mFrameCount: Int
    private val mFrameBitmap: Array<Bitmap>
    private val mIsLoop: Boolean
    private var animIndex = 0

    constructor(frameBitmap: Bitmap, isLoop: Boolean) : this(arrayOf<Bitmap>(frameBitmap), isLoop) {}

    //重置动画
    fun reset() {
        mPlayID = 0
        isEnd = false
        isPause = false
    }

    fun pause() {
        isPause = true
    }

    fun resume() {
        isPause = false
    }

    /**
     * 绘制动画中的其中一帧
     */
    fun drawFrame(canvas: Canvas, paint: Paint?, x: Int, y: Int, frameID: Int) {
        canvas.drawBitmap(mFrameBitmap[frameID], x.toFloat(), y.toFloat(), paint)
    }

    /**
     * 绘制动画
     */
    fun drawAnimation(canvas: Canvas, paint: Paint?, x: Int, y: Int) {
        if (!isEnd && !isPause) {
            canvas.drawBitmap(mFrameBitmap[mPlayID], x.toFloat(), y.toFloat(), paint)
            if (animIndex == 2) {
                mPlayID++
                animIndex = 0
            } else {
                animIndex++
            }
            if (mPlayID >= mFrameCount) {
                //标志动画播放结束
                isEnd = true
                if (mIsLoop) {
                    //设置循环播放
                    isEnd = false
                    mPlayID = 0
                }
            }
        }
    }

    /**
     * 读取图片资源
     */
    fun readBitmap(context: Context, resId: Int): Bitmap {
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.RGB_565
        // 获取资源图片
        val `is` = context.resources.openRawResource(resId)
        return BitmapFactory.decodeStream(`is`, null, opt)
    }

    init {
        mFrameCount = frameBitmap.size
        mFrameBitmap = frameBitmap
        mIsLoop = isLoop
        reset()
    }
}