package com.jqorz.planewar.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.InputStream;


public class AnimationImpl {

    public boolean isEnd = false;
    private int mPlayID = 0;
    private int mFrameCount;
    private Bitmap[] mFrameBitmap;
    private boolean mIsLoop;

    public AnimationImpl(Bitmap frameBitmap, boolean isLoop) {
        this(new Bitmap[]{frameBitmap}, isLoop);
    }

    public AnimationImpl(Bitmap[] frameBitmap, boolean isLoop) {
        mFrameCount = frameBitmap.length;
        mFrameBitmap = frameBitmap;
        mIsLoop = isLoop;
    }

    //重置动画
    public void reset() {
        mPlayID = 0;
        isEnd = false;
    }

    public void pause() {
        isEnd = true;
    }

    public void resume() {
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
        if (!isEnd) {
            canvas.drawBitmap(mFrameBitmap[mPlayID], x, y, paint);
            mPlayID++;
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

    /**
     * 读取图片资源
     */
    public Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
