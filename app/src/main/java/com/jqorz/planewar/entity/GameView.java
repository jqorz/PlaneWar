package com.jqorz.planewar.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jqorz.planewar.frame.GamePlaying;
import com.jqorz.planewar.listener.GameListener;
import com.jqorz.planewar.manager.DrawManager;
import com.jqorz.planewar.manager.MoveManager;
import com.jqorz.planewar.manager.StatusManager;
import com.jqorz.planewar.thread.MainRunThread;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.tools.DeviceTools;
import com.jqorz.planewar.utils.Logg;

import java.util.ArrayList;

/**
 * 主游戏界面控制类
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, MainRunThread.OnThreadRunListener {


    private static final int clickLimitValue = 5;
    public BulletSupply mBulletSupply;//子弹补给
    public BombSupply mBombSupply;//炸弹补给
    public BgEntity[] mBgEntityArray;
    public HeroPlane heroPlane;
    public MainRunThread mMainRunThread;//刷帧的线程
    public ArrayList<Bullet> mBullets = new ArrayList<>();//子弹数组
    public ArrayList<EnemyPlane> mEnemyPlanes = new ArrayList<>();//敌军飞机数组
    public GamePlaying activity;
    public Paint mBgPaint = new Paint();
    public Paint mHeroPlanePaint = new Paint();
    public Paint mEmptyPlanePaint = new Paint();
    public Paint mSupplyPlanePaint = new Paint();
    public Paint mBulletPlanePaint = new Paint();

    private StatusManager mStatusManager;
    private MoveManager mMoveManager;
    private DrawManager mDrawManager;
    private float dX, dY = 0f;
    private float downX, downY = 0f;
    private GameListener mGameListener;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (GamePlaying) context;
        this.setKeepScreenOn(true);//保持屏幕常亮
        this.setFocusableInTouchMode(false);//设置不允许按键
        BitmapLoader.init(context);
        initGameView();

    }

    public void initGameView() {

        getHolder().addCallback(this);//注册接口

        mMainRunThread = new MainRunThread(this, getHolder());//初始化刷帧线程

        heroPlane = new HeroPlane();//初始化我方飞机

        mBulletSupply = new BulletSupply();//取子弹补给
        mBombSupply = new BombSupply(); //取炸弹补给

        mStatusManager = new StatusManager(this);
        mMoveManager = new MoveManager(this);
        mDrawManager = new DrawManager(this);

        initBgArray();

    }

    private void initBgArray() {
        int bgSize = (int) Math.ceil(DeviceTools.getScreenHeight() / (BitmapLoader.background.getHeight() * 1.0f)) + 1;
        Logg.i("屏幕高度=" + DeviceTools.getScreenHeight() + " 背景高度=" + BitmapLoader.background.getHeight() + " 背景图数量=" + bgSize);
        mBgEntityArray = new BgEntity[bgSize];

        for (int i = 0; i < mBgEntityArray.length; i++) {
            BgEntity bgEntity = new BgEntity();
            if (i == 0) {
                bgEntity.setY(DeviceTools.getScreenHeight() - bgEntity.getHeight());
            } else {
                BgEntity lastBg = mBgEntityArray[i - 1];
                bgEntity.setY(lastBg.getY() - bgEntity.getHeight());
            }
            mBgEntityArray[i] = bgEntity;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mMainRunThread.isPlaying) {
            return super.onTouchEvent(event);
        }
        float X = event.getRawX();
        float Y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = X;
                downY = Y;
                dX = heroPlane.getX() - event.getRawX();
                dY = heroPlane.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!(Math.abs(X - downX) < clickLimitValue) || !(Math.abs(Y - downY) < clickLimitValue)) {
                    heroPlane.setX((int) (event.getRawX() + dX));
                    heroPlane.setY((int) (event.getRawY() + dY));
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {//创建时启动相应进程

        this.mMainRunThread.setFlag(true);//启动刷帧线程
        this.mMainRunThread.start();


    }

    public void surfaceDestroyed(SurfaceHolder holder) {//摧毁时释放相应进程
        boolean retry = true;
        this.mMainRunThread.setFlag(false);
        while (retry) {
            try {
                mMainRunThread.join();
                retry = false;
            } catch (InterruptedException e) {//不断地循环，直到刷帧线程结束
            }
        }
    }

    @Override
    public void onThreadTime() {
        mMoveManager.moveEntity();
        mStatusManager.checkStatus();
    }

    @Override
    public void onDrawTime(Canvas canvas) {
        mDrawManager.drawBg(canvas);
        mDrawManager.drawEntity(canvas);
    }

    public void setBomb() {
        mStatusManager.useBomb = true;
    }

    public void onHeroDie() {
        if (mGameListener != null) {
            mGameListener.onHeroDie();
        }
    }

    public void onGameOver() {
        if (mGameListener != null) {
            mGameListener.onGameOver();
        }
        mMainRunThread.setFlag(false);
    }

    public void onEnemyAttacked(int type) {
        if (mGameListener != null) {
            mGameListener.onEnemyAttacked(type);
        }
    }

    public void onHeroAttacked() {
        if (mGameListener != null) {
            mGameListener.onHeroAttacked();
        }
    }

    public void onEnemyDie(int type) {
        if (mGameListener != null) {
            mGameListener.onEnemyDie(type);
        }
    }

    public void onGetBomb(int count) {
        if (mGameListener != null) {
            mGameListener.onGetBomb(count);
        }
    }

    public void setGameListener(GameListener gameListener) {
        mGameListener = gameListener;
    }
}