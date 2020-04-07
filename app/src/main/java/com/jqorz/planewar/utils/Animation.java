package com.jqorz.planewar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.InputStream;

/**
 * copyright datedu
 *
 * @author j1997
 * @since 2020/4/7
 */
public class Animation {

    /**
     * 动画播放间隙时间
     **/
    private static final int ANIM_TIME = 100;
    /**
     * 播放结束
     **/
    public boolean isEnd = false;
    /**
     * 上一帧播放时间
     **/
    private long mLastPlayTime = 0;
    /**
     * 播放当前帧的ID
     **/
    private int mPlayID = 0;
    /**
     * 动画frame数量
     **/
    private int mFrameCount = 0;
    /**
     * 用于储存动画资源图片
     **/
    private Bitmap[] mFrameBitmap = null;
    /**
     * 是否循环播放
     **/
    private boolean mIsLoop = false;


    public Animation(Context context, int[] frameBitmapID, boolean isLoop) {
        mFrameCount = frameBitmapID.length;
        mFrameBitmap = new Bitmap[mFrameCount];
        for (int i = 0; i < mFrameCount; i++) {
            mFrameBitmap[i] = readBitmap(context, frameBitmapID[i]);
        }
        mIsLoop = isLoop;
    }

    public Animation(Context context, Bitmap[] frameBitmap, boolean isLoop) {
        mFrameCount = frameBitmap.length;
        mFrameBitmap = frameBitmap;
        mIsLoop = isLoop;
    }

    //重置动画
    public void reset() {
        mLastPlayTime = 0;
        mPlayID = 0;
        isEnd = false;
    }

    /**
     * 绘制动画中的其中一帧
     */
    public void drawFrame(Canvas canvas, Paint paint, int x, int y, int frameID) {
        canvas.drawBitmap(mFrameBitmap[frameID], x, y, paint);
    }


    /**
     * 绘制动画
     */
    public void drawAnimation(Canvas canvas, Paint paint, int x, int y) {
        //如果没有播放结束则继续播放
        if (!isEnd) {
            canvas.drawBitmap(mFrameBitmap[mPlayID], x, y, paint);
            long time = System.currentTimeMillis();
            if (time - mLastPlayTime > ANIM_TIME) {
                mPlayID++;
                mLastPlayTime = time;
                if (mPlayID >= mFrameCount) {
                    //标志动画播放结束
                    isEnd = true;
                    if (mIsLoop) {
                        //设置循环播放
                        isEnd = false;
                        mPlayID = 0;
                    }
                }
            }
        }
    }

    /**
     * 读取图片资源
     */
    public Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
