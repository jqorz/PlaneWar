package com.jqorz.planewar.listener

/**
 * @author j1997
 * @since 2020/5/2
 */
interface GameListener {
    fun onHeroAttacked()
    fun onHeroDie()
    fun onEnemyDie(type: Int)
    fun onEnemyAttacked(type: Int)
    fun onGetBomb(count: Int)
    fun onGameOver()
}