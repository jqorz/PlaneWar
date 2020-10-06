package com.jqorz.planewar.tools

import com.jqorz.planewar.base.BaseSupply
import com.jqorz.planewar.constant.BulletType
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.constant.PlaneType
import com.jqorz.planewar.entity.HeroPlane
import java.util.*

object MapCreator {

    //如果类型为3，取当前速度//如果类型为2，在当前速度与最大速度取一个值//如果类型为1，在当前速度与最大速度取一个值//概率获得飞机类型//飞机飞行速度
    fun getNewSupplyPos(supply: BaseSupply): Int {
        return Random().nextInt(DeviceTools.getScreenWidth() - supply.width)
    }

    //随机飞机位置
    fun getNewEnemyPlaneInfo(): PlaneInfo {
        val ra1 = Random()
        val x: Int //随机飞机位置
        val velocity: Int //飞机飞行速度
        val d1 = ra1.nextDouble()
        val type: PlaneType //概率获得飞机类型
        type = when {
            d1 <= 0.1 -> {
                PlaneType.ENEMY_TYPE3
            }
            d1 <= 0.4 -> {
                PlaneType.ENEMY_TYPE2
            }
            else -> {
                PlaneType.ENEMY_TYPE1
            }
        }
        velocity = when {
            type === PlaneType.ENEMY_TYPE1 -> { //如果类型为1，在当前速度与最大速度取一个值
                ra1.nextInt(ConstantValue.ENEMY_VELOCITY_MAX1 - ConstantValue.ENEMY_VELOCITY) + ConstantValue.ENEMY_VELOCITY
            }
            type === PlaneType.ENEMY_TYPE2 -> { //如果类型为2，在当前速度与最大速度取一个值
                ra1.nextInt(ConstantValue.ENEMY_VELOCITY_MAX2 - ConstantValue.ENEMY_VELOCITY) + ConstantValue.ENEMY_VELOCITY
            }
            else -> { //如果类型为3，取当前速度
                ConstantValue.ENEMY_VELOCITY
            }
        }
        x = when {
            type === PlaneType.ENEMY_TYPE1 -> {
                ra1.nextInt(DeviceTools.getScreenWidth() - BitmapLoader.bmps_enemyPlane1[0].width)
            }
            type === PlaneType.ENEMY_TYPE2 -> {
                ra1.nextInt(DeviceTools.getScreenWidth() - BitmapLoader.bmps_enemyPlane2[0].width)
            }
            else -> {
                ra1.nextInt(DeviceTools.getScreenWidth() - BitmapLoader.bmps_enemyPlane3[0].width)
            }
        }
        return PlaneInfo(x, velocity, type)
    }

    fun getNewBulletPos(heroPlane: HeroPlane): List<BulletInfo> {
        val infoList: MutableList<BulletInfo> = ArrayList()
        val x = heroPlane.x + ((heroPlane.width - BitmapLoader.bmp_bullet1[0].width) / 2.0).toInt()
        val y = heroPlane.y - ConstantValue.BULLET_SPAN / 2
        if (heroPlane.bulletType === BulletType.BULLET_RED) {
            val b1 = BulletInfo(x, y, BulletType.BULLET_RED)
            infoList.add(b1)
        } else if (heroPlane.bulletType === BulletType.BULLET_BLUE) {
            val b1 = BulletInfo(x, y - ConstantValue.BULLET_SPAN, BulletType.BULLET_BLUE)
            infoList.add(b1)
            val b2 = BulletInfo(x - ConstantValue.BULLET_SPAN, y, BulletType.BULLET_BLUE)
            infoList.add(b2)
            val b3 = BulletInfo(x + ConstantValue.BULLET_SPAN, y, BulletType.BULLET_BLUE)
            infoList.add(b3)
        }
        return infoList
    }

    class BulletInfo(val x: Int, val y: Int, val type: BulletType)
    class PlaneInfo(val x: Int, val velocity: Int, val type: PlaneType)
}