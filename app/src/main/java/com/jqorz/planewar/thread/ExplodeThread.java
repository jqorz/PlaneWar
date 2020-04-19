package com.jqorz.planewar.thread;

import com.jqorz.planewar.entity.GameView;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为爆炸的换帧线程
 * 每隔一段时间对GameView中explodeList中的爆炸换一帧
 */

public class ExplodeThread extends Thread {
    GameView gameView;
    private boolean flag = true;//循环标记位


    public ExplodeThread(GameView gameView) {//构造器
        this.gameView = gameView;
    }

    public void setFlag(boolean flag) {//设置循环标记位
        this.flag = flag;
    }

    public void run() {
        while (flag) {
            try {//防止并发访问的异常
                for (Explode e : gameView.explodeList) {
                    if (!e.nextFrame()) {
                        //当没有下一帧时删除该爆炸
                        gameView.explodeList.remove(e);
                    }
                }
            } catch (Exception e) {
            }
            try {
                Thread.sleep(ConstantUtil.EXPLORE_THREAD_SPAN);//睡眠休息
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}