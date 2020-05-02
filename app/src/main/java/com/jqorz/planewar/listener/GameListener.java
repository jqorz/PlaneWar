package com.jqorz.planewar.listener;

/**
 * @author j1997
 * @since 2020/5/2
 */
public interface GameListener {
    void onGameOver();

    void onGetBomb(int count);

    void onScoreAdd(int score);

    void onPlaySound();

}
