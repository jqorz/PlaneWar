package com.jqorz.planewar.manager

import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.entity.GameView
import com.jqorz.planewar.tools.MapCreator
import com.jqorz.planewar.tools.MapCreator.BulletInfo
import com.jqorz.planewar.tools.MapCreator.PlaneInfo
import com.jqorz.planewar.tools.TimeTools

/**
 * @author j1997
 * @since 2020/4/19
 */
class TimeManager     //构造器
(private val gameView: GameView, private val mStatusManager: StatusManager) {
    private var mSendTime = 0L //上一颗子弹发射的时间
    private var mEnemyTime = 0L //上一次敌军出现的时间
    private var mTime = 0L //计算敌机出现用的基准时间
    private var mDifficultyTime = 0L //计算难度用的时间
    private var mBombTime = 0L //计算炸弹补给出现的时间
    private var mChangeBulletTime = 0L //计算子弹补给出现的时间
    private var lastGetBulletSupplyTime = 0L
    private var mOnEntityChangeListener: OnEntityChangeListener? = null
    fun initTime() {
        val time = TimeTools.getCurrentTime()
        mTime = time
        mDifficultyTime = time
        mBombTime = time
        mChangeBulletTime = time
    }

    fun onGetBulletSupply() {
        lastGetBulletSupplyTime = TimeTools.getCurrentTime()
    }

    fun checkNew() {
        if (mOnEntityChangeListener == null) {
            return
        }
        val now = TimeTools.getCurrentTime()

        //检查是否应该显示敌人
        if (now - mTime >= ConstantValue.FIRST_ENEMY_TIME && !mStatusManager.showEnemy) {
            mOnEntityChangeListener!!.onFirstShowEnemy()
        }
        if (!mStatusManager.showHero) {
            mOnEntityChangeListener!!.onFirstShowHero()
        }

        //检查是否应该显示新敌人
        if (mStatusManager.showEnemy && now - mEnemyTime >= ConstantValue.ENEMY_TIME) {
            val info = MapCreator.getNewEnemyPlaneInfo()
            mOnEntityChangeListener!!.onNewEnemy(info)
            mEnemyTime = now
        }

        //计算炸弹补给时间
        if (now - mBombTime >= ConstantValue.SUPPLY_BOMB_INTERVAL_TIME) {
            mOnEntityChangeListener!!.onShowBombSupply()
            mBombTime = now
        }

        //计算子弹补给时间
        if (now - mChangeBulletTime >= ConstantValue.SUPPLY_BULLET_INTERVAL_TIME) {
            mOnEntityChangeListener!!.onShowBulletSupply()
            mChangeBulletTime = now
        }

        //计算上次发送子弹的时间
        if ((gameView.heroPlane.isFly || gameView.heroPlane.isInjure) && now - mSendTime >= ConstantValue.BULLET_TIME) {
            for (info in MapCreator.getNewBulletPos(gameView.heroPlane)) {
                mOnEntityChangeListener!!.onNewBullet(info)
            }
            mSendTime = now
        }

        //检查子弹补给的持续时间
        if (now - lastGetBulletSupplyTime > ConstantValue.SUPPLY_BULLET_LONG_TIME) {
            mOnEntityChangeListener!!.onBulletSupplyEnd()
        }
    }

    private fun addDifficulty(now: Long) {
        if (now - mDifficultyTime > ConstantValue.ENEMY_VELOCITY_AND_TIME) { //每隔一段时间
            if (ConstantValue.ENEMY_TIME > ConstantValue.ENEMY_TIME_MIN) { //如果时间仍然大于最小时间
                ConstantValue.ENEMY_TIME = ConstantValue.ENEMY_TIME - ConstantValue.ENEMY_TIME_MUL //当前时间递减
            } else {
                ConstantValue.ENEMY_TIME = ConstantValue.ENEMY_TIME_MIN //否则就为最小时间
            }
            if (ConstantValue.ENEMY_VELOCITY < ConstantValue.ENEMY_VELOCITY_ADD_MAX) { //如果速度仍然小于最大速度
                ConstantValue.ENEMY_VELOCITY = ConstantValue.ENEMY_VELOCITY + ConstantValue.ENEMY_VELOCITY_ADD //当前速度递增
            } else {
                ConstantValue.ENEMY_VELOCITY = ConstantValue.ENEMY_VELOCITY_ADD_MAX //否则就为最大速度
            }
            mDifficultyTime = now
        }
    }

    fun setOnEntityChangeListener(onEntityChangeListener: OnEntityChangeListener?) {
        mOnEntityChangeListener = onEntityChangeListener
    }

    interface OnEntityChangeListener {
        fun onNewEnemy(planeInfo: PlaneInfo)
        fun onNewBullet(bulletInfo: BulletInfo)
        fun onShowBulletSupply()
        fun onShowBombSupply()
        fun onFirstShowEnemy()
        fun onFirstShowHero()
        fun onBulletSupplyEnd()
    }
}