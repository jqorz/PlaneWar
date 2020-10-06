package com.jqorz.planewar.entity

import com.jqorz.planewar.base.BaseSupply
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.tools.BitmapLoader
import com.jqorz.planewar.tools.MapCreator

/**
 * 该类为炸弹补给类
 */
class BombSupply internal constructor() : BaseSupply(BitmapLoader.bmp_bombSupply) {
    override fun reset() {
        setX(MapCreator.getNewSupplyPos(this))
        setY(-height)
        setShown(true)
    }

    override fun move() {
        y += ConstantValue.Bomb_Velocity
    }
}