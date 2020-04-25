package com.jqorz.planewar.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jqorz.planewar.R;
import com.jqorz.planewar.eenum.PlaneStatus;
import com.jqorz.planewar.eenum.PlaneType;
import com.jqorz.planewar.frame.GamePlaying;
import com.jqorz.planewar.manager.DrawManager;
import com.jqorz.planewar.manager.MoveManager;
import com.jqorz.planewar.manager.StatusManager;
import com.jqorz.planewar.thread.MainRunThread;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.tools.DeviceTools;
import com.jqorz.planewar.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 主游戏界面控制类
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, MainRunThread.OnThreadRunListener {


    public BulletSupply mBulletSupply;//子弹补给
    public BombSupply mBombSupply;//炸弹补给
    public BgEntity mBgEntity1, mBgEntity2;
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

    private SoundPool soundPool;//声音
    private HashMap<Integer, Integer> soundPoolMap;
    private StatusManager mStatusManager;
    private MoveManager mMoveManager;
    private DrawManager mDrawManager;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (GamePlaying) context;
        this.setKeepScreenOn(true);//保持屏幕常亮
        this.setFocusableInTouchMode(false);//设置不允许按键
        BitmapLoader.init(context);
        initSounds();
        initGameView();

    }

    public void initGameView() {
        int screenHeight = DeviceTools.getScreenHeight();

        getHolder().addCallback(this);//注册接口

        mMainRunThread = new MainRunThread(this, getHolder());//初始化刷帧线程

        heroPlane = new HeroPlane();//初始化我方飞机

        mBulletSupply = new BulletSupply();//取子弹补给
        mBombSupply = new BombSupply(); //取炸弹补给

        mBgEntity1 = new BgEntity();
        mBgEntity2 = new BgEntity();

        mBgEntity1.setY(Math.abs(mBgEntity1.getHeight() - screenHeight));
        mBgEntity2.setY(mBgEntity1.getY() - mBgEntity1.getHeight());

        mStatusManager = new StatusManager(this);
        mMoveManager = new MoveManager(this);
        mDrawManager = new DrawManager(this);

    }

    private void initSounds() {
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
                .setMaxStreams(4)
                .build();
        soundPoolMap = new HashMap<>();
        soundPoolMap.put(2, soundPool.load(getContext(), R.raw.attack, 1));
        soundPoolMap.put(3, soundPool.load(getContext(), R.raw.boom_noraml, 1));
        soundPoolMap.put(4, soundPool.load(getContext(), R.raw.boom_big, 1));
    }

    public void playSound(int sound, int loop) {
        if (activity.getIsSound()) {
            AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
            float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = streamVolumeCurrent / streamVolumeMax;
            soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
        }
    }


    public void setBomb() {

        for (EnemyPlane ep : mEnemyPlanes) {
            if (ep.isFly() || ep.isInjure()) {
                ep.setStatus(PlaneStatus.STATUS_EXPLORE);
                Message msg = activity.myHandler.obtainMessage();
                switch (ep.getType()) {
                    case PlaneType.ENEMY_TYPE1:
                        playSound(2, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE1_SCORE;
                        break;
                    case PlaneType.ENEMY_TYPE2:
                        playSound(3, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE2_SCORE;
                        break;
                    case PlaneType.ENEMY_TYPE3:
                        playSound(4, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE3_SCORE;
                        break;
                }
                activity.myHandler.sendMessage(msg);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        int x=heroPlane.getX();
//        int y=heroPlane.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) { //处理屏幕屏点下事件 手指点击屏幕时触发
//            xx = (int)event.getX();
//            yy = (int)event.getY();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {//处理移动事件 手指在屏幕上移动时触发

            int Px = (int) event.getX();
            int Py = (int) event.getY();

            heroPlane.setX(Px - heroPlane.getWidth() / 2);
            heroPlane.setY(Py - heroPlane.getHeight());
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

        soundPool.release();
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
        mDrawManager.drawEntity(canvas);
    }
}