package com.jqorz.planewar.entity

import com.jqorz.planewar.base.BaseSupply
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.tools.BitmapLoader
import com.jqorz.planewar.tools.MapCreator

/**
 * 该类为炸弹补给类
 */
class BombSupply : BaseSupply(BitmapLoader.bmp_bombSupply) {

    override fun reset() {
        x = (MapCreator.getNewSupplyPos(this))
        y = (-height)
        isHide = false
    }

    override fun move() {
        y += ConstantValue.Bomb_Velocity
    }
}