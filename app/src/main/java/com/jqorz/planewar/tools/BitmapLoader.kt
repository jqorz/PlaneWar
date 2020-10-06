package com.jqorz.planewar.tools

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jqorz.planewar.R

/**
 * @author j1997
 * @since 2020/4/19
 */
object BitmapLoader {
    lateinit var bmps_enemyPlane1_explode: Array<Bitmap>   //敌机1爆炸的数组
    lateinit var bmps_enemyPlane2_explode: Array<Bitmap> //敌机2爆炸的数组
    lateinit var bmps_enemyPlane3_explode: Array<Bitmap> //敌机3爆炸的数组
    lateinit var bmps_hero_explode: Array<Bitmap>//英雄飞机爆炸的数组
    lateinit var bmp_enemyPlane2_injured: Array<Bitmap>//敌机2受伤的图片
    lateinit var bmp_enemyPlane3_injured: Array<Bitmap>//敌机3受伤的图片
    lateinit var bmps_enemyPlane1: Array<Bitmap> //敌机1飞行的数组
    lateinit var bmps_enemyPlane2: Array<Bitmap> //敌机2飞行的数组
    lateinit var bmps_enemyPlane3: Array<Bitmap> //敌机3飞行的数组
    lateinit var bmps_heroPlane: Array<Bitmap> //英雄飞机飞行的数组
    lateinit var bmp_bulletSupply: Array<Bitmap>//子弹补给的图片
    lateinit var bmp_bombSupply: Array<Bitmap>//炸弹补给的图片
    lateinit var bmp_bullet1: Array<Bitmap>
    lateinit var bmp_bullet2: Array<Bitmap>
    lateinit var background: Bitmap //背景的大图元

    fun init() {
        val resource: Resources = DeviceTools.getApp().resources
        //飞机的图片
        bmps_heroPlane = decodeRes(resource, R.drawable.playing_hero_run1, R.drawable.playing_hero_run2)
        bmps_enemyPlane1 = decodeRes(resource, R.drawable.playing_enemy1_run)
        bmps_enemyPlane2 = decodeRes(resource, R.drawable.playing_enemy2_run)
        bmps_enemyPlane3 = decodeRes(resource, R.drawable.playing_enemy3_run1, R.drawable.playing_enemy3_run2)

        bmp_bombSupply = decodeRes(resource, R.drawable.playing_supply_bomb)
        bmp_bulletSupply = decodeRes(resource, R.drawable.playing_supply_bullet)
        bmp_bullet1 = decodeRes(resource, R.drawable.playing_bullet_red)
        bmp_bullet2 = decodeRes(resource, R.drawable.playing_bullet_blue)
        bmp_enemyPlane2_injured = decodeRes(resource, R.drawable.playing_enemy2_injured)
        bmp_enemyPlane3_injured = decodeRes(resource, R.drawable.playing_enemy3_injured)

        //飞机爆炸图片
        bmps_hero_explode = decodeRes(resource, R.drawable.playing_hero_dead1, R.drawable.playing_hero_dead2, R.drawable.playing_hero_dead3, R.drawable.playing_hero_dead4)
        bmps_enemyPlane1_explode = decodeRes(resource, R.drawable.playing_enemy1_dead1, R.drawable.playing_enemy1_dead2, R.drawable.playing_enemy1_dead3, R.drawable.playing_enemy1_dead4)
        bmps_enemyPlane2_explode = decodeRes(resource, R.drawable.playing_enemy2_dead1, R.drawable.playing_enemy2_dead2, R.drawable.playing_enemy2_dead3, R.drawable.playing_enemy2_dead4)
        bmps_enemyPlane3_explode = decodeRes(resource, R.drawable.playing_enemy3_dead1, R.drawable.playing_enemy3_dead2, R.drawable.playing_enemy3_dead3, R.drawable.playing_enemy3_dead4, R.drawable.playing_enemy3_dead5, R.drawable.playing_enemy3_dead6, R.drawable.playing_enemy3_dead7)

        //大背景图片
        val bmp = BitmapFactory.decodeResource(resource, R.drawable.game_background)
        val toWidth = DeviceTools.getScreenWidth()
        var toHeight = DeviceTools.getScreenHeight()
        if (bmp.width != 0) {
            toHeight = toWidth * bmp.height / bmp.width
        }
        //按照背景图的宽高比，将背景图的宽度缩放至屏幕宽度，高度等比缩放
        background = Bitmap.createScaledBitmap(bmp, toWidth, toHeight, true)
    }

    private fun decodeRes(resource: Resources, vararg ids: Int): Array<Bitmap> {
        return Array(ids.size) {
            BitmapFactory.decodeResource(resource, ids[it])
        }
    }


}