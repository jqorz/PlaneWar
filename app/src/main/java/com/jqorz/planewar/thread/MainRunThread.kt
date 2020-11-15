package com.jqorz.planewar.thread

import android.graphics.Canvas
import android.view.SurfaceHolder
import com.jqorz.planewar.utils.Logg

/**
 * 刷帧线程
 */
class MainRunThread(private val mListener: OnThreadRunListener?, private val mSurfaceHolder: SurfaceHolder) : Thread() {
    var isPlaying = true
    var flag = false//设置循环标记位


    override fun run() {
        var c: Canvas?
        while (flag) {
            c = null
            try {
                if (isPlaying) {
                    mListener?.onThreadTime()
                    // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    c = mSurfaceHolder.lockCanvas(null)
                    synchronized(mSurfaceHolder) { mListener?.onDrawTime(c) }
                }
            } catch (e: Exception) {
                Logg.e("thread", e)
            } finally {
                if (c != null) {
                    //更新屏幕显示内容
                    mSurfaceHolder.unlockCanvasAndPost(c)
                }
            }
        }
    }

    interface OnThreadRunListener {
        fun onThreadTime()
        fun onDrawTime(canvas: Canvas)
    }
}