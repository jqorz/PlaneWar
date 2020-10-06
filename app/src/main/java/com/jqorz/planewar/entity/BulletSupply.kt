package com.jqorz.planewar.entity

import com.jqorz.planewar.base.BaseSupply
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.tools.BitmapLoader
import com.jqorz.planewar.tools.MapCreator

/**
 * 该类为子弹补给类
 */
class BulletSupply internal constructor() : BaseSupply(BitmapLoader.bmp_bulletSupply) {
    override fun reset() {
        setX(MapCreator.getNewSupplyPos(this))
        setY(-height)
    }

    override fun move() {
        y += ConstantValue.ChangeBullet_Velocity
    }
}