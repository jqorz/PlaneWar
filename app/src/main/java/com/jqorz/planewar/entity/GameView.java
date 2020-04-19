package com.jqorz.planewar.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jqorz.planewar.R;
import com.jqorz.planewar.eenum.BulletType;
import com.jqorz.planewar.eenum.PlaneStatus;
import com.jqorz.planewar.eenum.PlaneType;
import com.jqorz.planewar.frame.GamePlaying;
import com.jqorz.planewar.tools.MapCreator;
import com.jqorz.planewar.thread.MainRunThread;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;
import com.jqorz.planewar.utils.Logg;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 主游戏界面控制类
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    public static int screenHeight, screenWidth;

    public BulletSupply mBulletSupply;//子弹补给
    public BombSupply mBombSupply;//炸弹补给
    public BgEntity mBgEntity1, mBgEntity2;
    public HeroPlane heroPlane;
    public MainRunThread mainThread;//刷帧的线程
    public int mEnemyId = 0; //初始化敌军ID
    public Long mSendTime = 0L; //上一颗子弹发射的时间
    public Long mEnemyTime = 0L; //上一次敌军出现的时间
    public Long mTime = 0L; //计算敌机出现,,用的基准时间
    public Long mDifficultyTime = 0L; //计算难度用的时间
    public Long mBombTime = 0L; //计算炸弹补给出现的时间
    public Long mChangeBulletTime = 0L; //计算子弹补给出现的时间
    public CopyOnWriteArrayList<Bullet> mBullets = new CopyOnWriteArrayList<>();//子弹数组
    public CopyOnWriteArrayList<EnemyPlane> mEnemyPlanes = new CopyOnWriteArrayList<>();//敌军飞机数组
    public GamePlaying activity;

    private Paint mBgPaint = new Paint();
    private Paint mHeroPlanePaint = new Paint();
    private Paint mEmptyPlanePaint = new Paint();
    private Paint mSupplyPlanePaint = new Paint();
    private Paint mBulletPlanePaint = new Paint();
    private SoundPool soundPool;//声音
    private HashMap<Integer, Integer> soundPoolMap;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (GamePlaying) context;
        this.setKeepScreenOn(true);//保持屏幕常亮
        this.setFocusableInTouchMode(false);//设置不允许按键
        BitmapLoader.init(context);
        initScreenSize();
        initSounds();
        initGameView();

    }

    private void initScreenSize() {
        Display display = activity.getWindowManager().getDefaultDisplay();//获取屏幕宽高
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
    }


    public void initGameView() {

        getHolder().addCallback(this);//注册接口

        this.mainThread = new MainRunThread(getHolder(), this);//初始化刷帧线程

//        mBullet = new Bullet[ConstantUtil.BULLET_POOL_COUNT];
        mBullets = new CopyOnWriteArrayList<>();
//        for (int i = 0; i < ConstantUtil.BULLET_POOL_COUNT; i++) {
//            mBullet[i] = new Bullet(0, 0);
//        }
        mEnemyPlanes = new CopyOnWriteArrayList<>();
        mTime = System.currentTimeMillis();
        mDifficultyTime = System.currentTimeMillis();
        mBombTime = System.currentTimeMillis();
        mChangeBulletTime = System.currentTimeMillis();
        mBulletSupply = new BulletSupply();//取子弹补给
        mBombSupply = new BombSupply(); //取炸弹补给

        mBgEntity1 = new BgEntity();
        mBgEntity2 = new BgEntity();

        mBgEntity1.setY(Math.abs(mBgEntity1.getHeight() - screenHeight));
        mBgEntity2.setY(mBgEntity1.getY() - mBgEntity1.getHeight());

        heroPlane = new HeroPlane();//初始化我方飞机
        heroPlane.setX((screenWidth - heroPlane.getWidth()) / 2);
        heroPlane.setY((screenHeight / 3 * 2 - heroPlane.getHeight()) / 2);
    }


    private void initSounds() {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
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


    public void mDraw(final Canvas canvas) {

        if (canvas != null) {
            //绘制游戏背景
            mBgEntity1.draw(canvas, mBgPaint);
            mBgEntity2.draw(canvas, mBgPaint);

            //绘制玩家飞机
            if (heroPlane.isShown()) {
                heroPlane.draw(canvas,mHeroPlanePaint);
            }

            //计算当前时间
            long now = activity.getCurrentTime();

            //绘制子弹及敌机
            if (now - mTime >= ConstantUtil.FIRST_ENEMY_TIME) {
                drawEnemy(canvas, now);
                if (heroPlane.isShown()) {
                    drawBullets(canvas, now);
                }
            }
            //绘制炸弹补给
            drawBomb(canvas, now);

            //绘制子弹补给
            drawChangeBullet(canvas, now);

            //计算难度
//            addDifficulty(now);

        }


    }

    private void drawChangeBullet(Canvas canvas, long now) {
        if (mBulletSupply.getShown()) {
            mBulletSupply.move();
        }
        //计算子弹补给时间
        if (now - mChangeBulletTime >= ConstantUtil.SUPPLY_BULLET_INTERVAL_TIME) {
            mBulletSupply.setShown(true);
            mChangeBulletTime = now;
        }


        if (mBulletSupply.getShown()) {
            mBulletSupply.draw(canvas,mSupplyPlanePaint);

        }
    }

    private void drawBomb(Canvas canvas, long now) {
        if (mBombSupply.getShown()) {
            mBombSupply.move();
        }
        //计算炸弹补给时间
        if (now - mBombTime >= ConstantUtil.SUPPLY_BOMB_INTERVAL_TIME) {
            mBombSupply.setShown(true);
            mBombTime = now;
        }
        //绘制炸弹补给
        if (mBombSupply.getShown()) {
            mBombSupply.draw(canvas,mSupplyPlanePaint);
        }

    }

    //通过计算时间来对敌机速度及时间间隔处理，增大难度
    private void addDifficulty(long now) {
        if (now - mDifficultyTime > ConstantUtil.ENEMY_VELOCITY_AND_TIME) {//每隔一段时间
            if (ConstantUtil.ENEMY_TIME > ConstantUtil.ENEMY_TIME_MIN) {//如果时间仍然大于最小时间
                ConstantUtil.ENEMY_TIME = ConstantUtil.ENEMY_TIME - ConstantUtil.ENEMY_TIME_MUL;//当前时间递减
            } else {
                ConstantUtil.ENEMY_TIME = ConstantUtil.ENEMY_TIME_MIN;//否则就为最小时间
            }
            if (ConstantUtil.ENEMY_VELOCITY < ConstantUtil.ENEMY_VELOCITY_ADD_MAX) {//如果速度仍然小于最大速度
                ConstantUtil.ENEMY_VELOCITY = ConstantUtil.ENEMY_VELOCITY + ConstantUtil.
                        ENEMY_VELOCITY_ADD;//当前速度递增
            } else {
                ConstantUtil.ENEMY_VELOCITY = ConstantUtil.ENEMY_VELOCITY_ADD_MAX;//否则就为最大速度
            }
            mDifficultyTime = now;
        }

    }

    //绘制敌人动画
    private void drawEnemy(Canvas canvas, long now) {

        //移动敌军飞机
        for (EnemyPlane ep : mEnemyPlanes) {
            ep.move();
        }


        //根据时间初始化敌军飞机
        if (mEnemyId < ConstantUtil.ENEMY_POOL_COUNT) {
            if (now - mEnemyTime >= ConstantUtil.ENEMY_TIME) {
                MapCreator.PlaneInfo info = MapCreator.getNewEnemyPlaneInfo();
                mEnemyPlanes[mEnemyId].setX(info.getX());
                mEnemyPlanes[mEnemyId].setY(-500);
                mEnemyPlanes[mEnemyId].setVelocity(info.getVelocity());
                mEnemyPlanes[mEnemyId].setType(info.getType());

                mEnemyTime = now;
                mEnemyPlanes[mEnemyId].setStatus(true);
                mEnemyId++;
            }
        } else {
            mEnemyId = 0;
        }

        //绘制状态正常的敌军飞机
        for (EnemyPlane ep : mEnemyPlanes) {
            if (ep.isShown()) {
                ep.draw(canvas,mEmptyPlanePaint);
            }
        }

    }

    private void drawBullets(Canvas canvas, long now) {


        //根据时间初始化未发射的子弹
        if (now - mSendTime >= ConstantUtil.BULLET_TIME) {
            int x = heroPlane.getX() + (int) ((bmps_heroPlane[0].getWidth() - bmp_bullet1.getWidth()) / 2.0);
            int y = heroPlane.getY() - ConstantUtil.BULLET_SPAN / 2;


            if (heroPlane.getBulletType() == BulletType.BULLET_RED) {
                Bullet b1 = new Bullet(BulletType.BULLET_RED);
                b1.setXY(x, y);
                mBullets.add(b1);

            } else if (heroPlane.getBulletType() == BulletType.BULLET_BLUE) {
                Bullet b1 = new Bullet(BulletType.BULLET_BLUE);
                b1.setXY(x, y - ConstantUtil.BULLET_SPAN);
                mBullets.add(b1);

                Bullet b2 = new Bullet(BulletType.BULLET_BLUE);
                b2.setXY(x - ConstantUtil.BULLET_SPAN, y);
                mBullets.add(b2);

                Bullet b3 = new Bullet(BulletType.BULLET_BLUE);
                b3.setXY(x + ConstantUtil.BULLET_SPAN, y);
                mBullets.add(b3);
            }
            mSendTime = now;
        }

        //移动子弹
        try {
            for (Bullet b : mBullets) {
                b.move();
            }
        } catch (Exception e) {
            Logg.e(e);
        }
        //绘制正常的子弹
        try {
            for (Bullet b : mBullets) {

                b.draw(canvas,mBulletPlanePaint);
            }
        } catch (Exception e) {
            Logg.e(e);
        }

    }

    public void setBomb() {

        for (EnemyPlane ep : mEnemyPlanes) {
            if (ep.isShown()) {
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

            heroPlane.setX(Px - heroPlane .getWidth() / 2);
            heroPlane.setY(Py - heroPlane .getHeight());
        }

        return true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {//创建时启动相应进程

        this.mainThread.setFlag(true);//启动刷帧线程
        this.mainThread.start();


    }

    public void surfaceDestroyed(SurfaceHolder holder) {//摧毁时释放相应进程
        boolean retry = true;
        this.mainThread.setFlag(false);

        soundPool.release();
        while (retry) {
            try {
                mainThread.join();
                retry = false;
            } catch (InterruptedException e) {//不断地循环，直到刷帧线程结束
            }
        }
    }


}