package com.jqorz.planewar.listener;

/**
 * @author j1997
 * @since 2020/5/2
 */
public interface GameListener {

    void onHeroAttacked();

    void onHeroDie();

    void onEnemyDie(int type);

    void onEnemyAttacked(int type);

    void onGetBomb(int count);

    void onGameOver();
}
