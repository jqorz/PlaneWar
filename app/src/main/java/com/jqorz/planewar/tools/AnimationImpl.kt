package com.jqorz.planewar.tools

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

class AnimationImpl(val mFrameBitmap: Array<Bitmap>, val isLoop: Boolean) {
    var isEnd = false
    var isPause = false
    private var mPlayID = 0
    private val mFrameCount: Int = mFrameBitmap.size
    private var animIndex = 0

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
                if (isLoop) {
                    //设置循环播放
                    isEnd = false
                    mPlayID = 0
                }
            }
        }
    }


}