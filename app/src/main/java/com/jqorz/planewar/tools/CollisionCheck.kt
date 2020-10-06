package com.jqorz.planewar.tools

import com.jqorz.planewar.base.BaseEntityImp

/**
 * @author j1997
 * @since 2020/4/19
 */
object CollisionCheck {
    private const val DEFAULT_OVERLAP_AREA = 0.2f

    fun isCollision(entity1: BaseEntityImp, entityImp2: BaseEntityImp): Boolean {
        return isCollision(entity1, entityImp2, DEFAULT_OVERLAP_AREA)
    }

    private fun isCollision(entity1: BaseEntityImp, entity2: BaseEntityImp, area: Float): Boolean { //判断两个矩形是否碰撞
        val xd: Int //大的x
        val yd: Int //大的y
        val xx: Int //小的x
        val yx: Int //小的y
        val width: Int
        val height: Int
        val xFlag: Boolean //entity1 x是否在前
        val yFlag: Boolean //entity1 y是否在前
        if (entity1.x >= entity2.x) {
            xd = entity1.x
            xx = entity2.x
            xFlag = false
        } else {
            xd = entity2.x
            xx = entity1.x
            xFlag = true
        }
        if (entity1.y >= entity2.y) {
            yd = entity1.y
            yx = entity2.y
            yFlag = false
        } else {
            yd = entity2.y
            yx = entity1.y
            yFlag = true
        }
        width = if (xFlag) {
            entity1.width
        } else {
            entity2.width
        }
        height = if (yFlag) {
            entity1.height
        } else {
            entity2.height
        }
        if (xd >= xx && xd <= xx + width - 1 && yd >= yx && yd <= yx + height - 1) { //首先判断两个矩形有否重叠
            val Dwidth = width - xd + xx.toDouble() //重叠区域宽度
            val Dheight = height - yd + yx.toDouble() //重叠区域高度
            //重叠面积超20%则判定为碰撞
            return Dwidth * Dheight / (entity2.height * entity2.width) >= area
        }
        return false
    }
}