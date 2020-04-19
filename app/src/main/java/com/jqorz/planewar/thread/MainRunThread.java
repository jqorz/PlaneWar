package com.jqorz.planewar.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * 刷帧线程
 */
public class MainRunThread extends Thread {
    private final SurfaceHolder mSurfaceHolder;
    public boolean flag2 = true;
    private boolean flag = false;
    private OnThreadRunListener mListener;

    public MainRunThread(OnThreadRunListener listener, SurfaceHolder surfaceHolder) {
        mListener = listener;
        mSurfaceHolder = surfaceHolder;
    }

    public void setFlag(boolean flag) {//设置循环标记位
        this.flag = flag;
    }

    @Override
    public void run() {
        Canvas c;

        while (this.flag) {
            if (mListener != null) {
                mListener.onThreadTime();
            }
            c = null;
            try {
                if (flag2) {
                    // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    c = mSurfaceHolder.lockCanvas(null);

                    synchronized (mSurfaceHolder) {
                        if (mListener != null) {
                            mListener.onDrawTime(c);
                        }
                    }
                }
            } finally {
                if (c != null) {
                    //更新屏幕显示内容
                    this.mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public interface OnThreadRunListener {
        void onThreadTime();

        void onDrawTime(Canvas canvas);
    }
}