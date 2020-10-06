package com.jqorz.planewar.listener

import com.jqorz.planewar.constant.PlaneType

/**
 * @author j1997
 * @since 2020/5/2
 */
interface GameListener {
    fun onHeroAttacked()
    fun onHeroDie()
    fun onEnemyDie(type: PlaneType)
    fun onEnemyAttacked(type: PlaneType)
    fun onGetBomb(count: Int)
    fun onGameOver()
}