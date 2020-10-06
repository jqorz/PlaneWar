package com.jqorz.planewar.entity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.jqorz.planewar.listener.GameListener
import com.jqorz.planewar.manager.DrawManager
import com.jqorz.planewar.manager.MoveManager
import com.jqorz.planewar.manager.StatusManager
import com.jqorz.planewar.thread.MainRunThread
import com.jqorz.planewar.thread.MainRunThread.OnThreadRunListener
import com.jqorz.planewar.tools.BitmapLoader
import com.jqorz.planewar.tools.DeviceTools
import com.jqorz.planewar.utils.Logg
import java.util.*

/**
 * 主游戏界面控制类
 */
class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback, OnThreadRunListener {
    var mBulletSupply: BulletSupply? = null//子弹补给
    lateinit var mBombSupply: BombSupply  //炸弹补给
    lateinit var mBgEntityArray: Array<BgEntity>
    lateinit var heroPlane: HeroPlane
    lateinit var mMainRunThread: MainRunThread //刷帧的线程
    var mBullets = ArrayList<Bullet>() //子弹数组
    var mEnemyPlanes = ArrayList<EnemyPlane>() //敌军飞机数组
    var mBgPaint = Paint()
    var mHeroPlanePaint = Paint()
    var mEmptyPlanePaint = Paint()
    var mSupplyPlanePaint = Paint()
    var mBulletPlanePaint = Paint()
    private lateinit var mStatusManager: StatusManager
    private lateinit var mMoveManager: MoveManager
    private lateinit var mDrawManager: DrawManager
    private var dX = 0f
    private var dY = 0f
    private var downX = 0f
    private var downY = 0f
    private var mGameListener: GameListener? = null
    fun initGameView() {
        holder.addCallback(this) //注册接口
        mMainRunThread = MainRunThread(this, holder) //初始化刷帧线程
        heroPlane = HeroPlane() //初始化我方飞机
        mBulletSupply = BulletSupply() //取子弹补给
        mBombSupply = BombSupply() //取炸弹补给
        mStatusManager = StatusManager(this)
        mMoveManager = MoveManager(this)
        mDrawManager = DrawManager(this)
        initBgArray()
    }

    private fun initBgArray() {
        val bgSize = Math.ceil(DeviceTools.getScreenHeight() / (BitmapLoader.background.height * 1.0f).toDouble()).toInt() + 1
        Logg.i("屏幕高度=" + DeviceTools.getScreenHeight() + " 背景高度=" + BitmapLoader.background.height + " 背景图数量=" + bgSize)
        mBgEntityArray = Array(bgSize) { BgEntity() }
        for (i in mBgEntityArray.indices) {
            val bgEntity = BgEntity()
            if (i == 0) {
                bgEntity.y = DeviceTools.getScreenHeight() - bgEntity.height
            } else {
                val lastBg = mBgEntityArray[i - 1]
                bgEntity.y = lastBg.y - bgEntity.height
            }
            mBgEntityArray[i] = bgEntity
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mMainRunThread.isPlaying) {
            return super.onTouchEvent(event)
        }
        val X = event.rawX
        val Y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = X
                downY = Y
                dX = heroPlane.x - event.rawX
                dY = heroPlane.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> if (Math.abs(X - downX) >= clickLimitValue || Math.abs(Y - downY) >= clickLimitValue) {
                heroPlane.x = (event.rawX + dX).toInt()
                heroPlane.y = (event.rawY + dY).toInt()
            }
            else -> return false
        }
        return true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceCreated(holder: SurfaceHolder) { //创建时启动相应进程
        mMainRunThread.flag = (true) //启动刷帧线程
        mMainRunThread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) { //摧毁时释放相应进程
        var retry = true
        mMainRunThread.flag = (false)
        while (retry) {
            try {
                mMainRunThread.join()
                retry = false
            } catch (e: InterruptedException) { //不断地循环，直到刷帧线程结束
            }
        }
    }

    override fun onThreadTime() {
        mMoveManager.moveEntity()
        mStatusManager.checkStatus()
    }

    override fun onDrawTime(canvas: Canvas) {
        mDrawManager.drawBg(canvas)
        mDrawManager.drawEntity(canvas)
    }

    fun setBomb() {
        mStatusManager.useBomb = true
    }

    fun onHeroDie() {
        mGameListener?.onHeroDie()
    }

    fun onGameOver() {
        mGameListener?.onGameOver()

        mMainRunThread.flag = (false)
    }

    fun onEnemyAttacked(type: Int) {
        mGameListener?.onEnemyAttacked(type)
    }

    fun onHeroAttacked() {
        mGameListener?.onHeroAttacked()

    }

    fun onEnemyDie(type: Int) {
        mGameListener?.onEnemyDie(type)

    }

    fun onGetBomb(count: Int) {
        mGameListener?.onGetBomb(count)
    }

    fun setGameListener(gameListener: GameListener?) {
        mGameListener = gameListener
    }

    companion object {
        private const val clickLimitValue = 5
    }

    init {
        this.keepScreenOn = true //保持屏幕常亮
        this.isFocusableInTouchMode = false //设置不允许按键
        BitmapLoader.init(context)
        initGameView()
    }
}