package com.jqorz.planewar.manager

import com.jqorz.planewar.GameView
import com.jqorz.planewar.constant.BulletType
import com.jqorz.planewar.constant.PlaneStatus
import com.jqorz.planewar.entity.Bullet
import com.jqorz.planewar.entity.EnemyPlane
import com.jqorz.planewar.manager.TimeManager.OnEntityChangeListener
import com.jqorz.planewar.tools.CollisionCheck
import com.jqorz.planewar.tools.DeviceTools
import com.jqorz.planewar.tools.MapCreator.BulletInfo
import com.jqorz.planewar.tools.MapCreator.PlaneInfo
import java.util.*

/**
 * @author j1997
 * @since 2020/4/12
 */
class StatusManager(private val gameView: GameView) : OnEntityChangeListener {
    var showEnemy = false
    var showHero = false
    var useBomb = false
    private val deleteBullets: MutableList<Bullet> = ArrayList()
    private val deleteEnemyPlanes: MutableList<EnemyPlane> = ArrayList()
    private val mTimeManager: TimeManager = TimeManager(gameView, this)

    fun checkStatus() {
        mTimeManager.checkNew()

        //检查越界
        for (bullet in gameView.mBullets) {
            if (bullet.isOutOfBound()) {
                bullet.isHide = true
            }
        }
        for (enemyPlane in gameView.mEnemyPlanes) {
            if (enemyPlane.isOutOfBound()) {
                enemyPlane.currentStatus = (PlaneStatus.STATUS_HIDE)
            }
        }
        val bombSupply = gameView.mBombSupply
        if (bombSupply.isOutOfBound()) {
            bombSupply.isHide = true
        }
        val bulletSupply = gameView.mBulletSupply
        if (bulletSupply.isOutOfBound()) {
            bulletSupply.isHide = true
        }
        val heroPlane = gameView.heroPlane

        //检查敌军飞机
        for (enemyPlane in gameView.mEnemyPlanes) {
            if (enemyPlane.isHide) {
                continue
            }
            if (useBomb) {
                if (enemyPlane.isFly() || enemyPlane.isInjure()) {
                    enemyPlane.currentStatus = (PlaneStatus.STATUS_INJURE)
                    enemyPlane.life = 0
                }
            }
            if (enemyPlane.isFly()) {
                for (bullet in gameView.mBullets) {
                    if (CollisionCheck.isCollision(enemyPlane, bullet)) { //打中敌机
                        enemyPlane.currentStatus = (PlaneStatus.STATUS_INJURE)
                        enemyPlane.life = enemyPlane.life - 1 //生命减1
                        bullet.isHide = true
                        gameView.onEnemyAttacked(enemyPlane.type)
                    }
                }
                if (CollisionCheck.isCollision(heroPlane, enemyPlane)) {
                    enemyPlane.currentStatus = (PlaneStatus.STATUS_INJURE)
                    enemyPlane.life = 0 //生命减1
                    heroPlane.setStatus(PlaneStatus.STATUS_INJURE)
                    heroPlane.life = 0
                    gameView.onHeroAttacked()
                }
            } else if (enemyPlane.isInjure()) {
                if (enemyPlane.life <= 0) { //当生命小于0时，向主线程发送得分信息
                    gameView.onEnemyDie(enemyPlane.type)
                    enemyPlane.currentStatus = (PlaneStatus.STATUS_EXPLORE)
                    gameView.onEnemyDie(enemyPlane.type)
                } else {
                    enemyPlane.currentStatus = (PlaneStatus.STATUS_FLY)
                    enemyPlane.resetInjureAnim()
                }
            } else if (enemyPlane.isExploreEnd()) {
                enemyPlane.currentStatus = (PlaneStatus.STATUS_HIDE)
            }
        }
        useBomb = false
        for (enemyPlane1 in gameView.mEnemyPlanes) {
            if (enemyPlane1.isHide) {
                deleteEnemyPlanes.add(enemyPlane1)
            }
        }
        gameView.mEnemyPlanes.removeAll(deleteEnemyPlanes)
        deleteEnemyPlanes.clear()
        for (bullet in gameView.mBullets) {
            if (bullet.isHide) {
                deleteBullets.add(bullet)
            }
        }
        gameView.mBullets.removeAll(deleteBullets)
        deleteBullets.clear()

        //检查英雄飞机状态
        if (heroPlane.isInjure()) {
            if (heroPlane.life <= 0) { //当生命小于0时
                heroPlane.setStatus(PlaneStatus.STATUS_EXPLORE)
                gameView.onHeroDie()
            } else {
                heroPlane.setStatus(PlaneStatus.STATUS_FLY)
                heroPlane.resetInjureAnim()
            }
        } else if (heroPlane.isExploreEnd()) {
            gameView.onGameOver()
        }

        //检查炸弹补给
        if (!bombSupply.isHide && !gameView.heroPlane.isHide) {
            if (CollisionCheck.isCollision(heroPlane, bombSupply)) {
                bombSupply.isHide = true
                gameView.onGetBomb(1)
            }
        }


        //检查子弹补给
        if (!bulletSupply.isHide && !gameView.heroPlane.isHide) {
            if (CollisionCheck.isCollision(heroPlane, bulletSupply)) {
                mTimeManager.onGetBulletSupply()
                heroPlane.bulletType = BulletType.BULLET_BLUE
                bulletSupply.isHide = true
            }
        }

        //检查背景
        for (i in gameView.mBgEntityArray.indices) {
            val bgEntity = gameView.mBgEntityArray[i]
            if (bgEntity.y > DeviceTools.getScreenHeight()) {
                //如果是第0个，应该把它排到第length-1后面，否则应该排到i-1后面
                val lastPos = if (i == 0) gameView.mBgEntityArray.size - 1 else i - 1
                bgEntity.y = gameView.mBgEntityArray[lastPos].y - bgEntity.height
            }
        }
    }

    override fun onNewEnemy(planeInfo: PlaneInfo) {
        val enemyPlane = EnemyPlane(planeInfo.type)
        enemyPlane.setVelocity(planeInfo.velocity)
        enemyPlane.currentStatus = (PlaneStatus.STATUS_FLY)
        enemyPlane.x = planeInfo.x
        gameView.mEnemyPlanes.add(enemyPlane)
    }

    override fun onNewBullet(bulletInfo: BulletInfo) {
        val bullet = Bullet(bulletInfo.type)
        bullet.setXY(bulletInfo.x, bulletInfo.y)
        bullet.isHide = false
        gameView.mBullets.add(bullet)
    }

    override fun onShowBulletSupply() {
        gameView.mBulletSupply.isHide = false
        gameView.mBulletSupply.reset()
    }

    override fun onShowBombSupply() {
        gameView.mBombSupply.isHide = false
        gameView.mBombSupply.reset()
    }

    override fun onFirstShowEnemy() {
        showEnemy = true
    }

    override fun onFirstShowHero() {
        showHero = true
        gameView.heroPlane.setStatus(PlaneStatus.STATUS_FLY)
    }

    override fun onBulletSupplyEnd() {
        gameView.heroPlane.bulletType = BulletType.BULLET_RED
    }

    init { //构造器
        mTimeManager.initTime()
        mTimeManager.setOnEntityChangeListener(this)
    }
}