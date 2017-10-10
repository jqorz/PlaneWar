package com.jqorz.planewar.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.jqorz.planewar.entity.GameView;

/**
 * 刷帧线程
 */
public class TutorialThread extends Thread {
    public boolean flag2 = true;
    int speed = 4;
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean flag = false;

    public TutorialThread(SurfaceHolder surfaceHolder, GameView gameView) {
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }


    public void setFlag(boolean flag) {//设置循环标记位
        this.flag = flag;
    }

    @Override
    public void run() {
        Canvas c;

        while (this.flag) {
            c = null;
            try {
                if (flag2) {
                    // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    c = this.surfaceHolder.lockCanvas(null);

                    synchronized (this.surfaceHolder) {

                        gameView.bg1y += speed;
                        gameView.bg2y += speed;
                        if (gameView.bg1y > GameView.screenHeight) {
                            gameView.bg1y = gameView.bg2y - GameView.screenHeight;
                        }
                        if (gameView.bg2y > GameView.screenHeight) {
                            gameView.bg2y = gameView.bg1y - GameView.screenHeight;
                        }
                        gameView.mDraw(c);
                    }
                }
            } finally {
                if (c != null) {
                    //更新屏幕显示内容
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}