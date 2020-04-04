package com.jqorz.planewar.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.jqorz.planewar.frame.GamePlaying;
import com.jqorz.planewar.thread.ExplodeThread;
import com.jqorz.planewar.thread.MoveThread;
import com.jqorz.planewar.thread.RunThread;
import com.jqorz.planewar.thread.TutorialThread;
import com.jqorz.planewar.utils.ConstantUtil;
import com.jqorz.planewar.utils.Logg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 主游戏界面控制类
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    public static int screenHeight, screenWidth;
    public static Bitmap[] bmps_enemyPlane1_explode = new Bitmap[4];//敌机1爆炸的数组
    public static Bitmap[] bmps_enemyPlane2_explode = new Bitmap[4];//敌机2爆炸的数组
    public static Bitmap[] bmps_enemyPlane3_explode = new Bitmap[6];//敌机3爆炸的数组
    public static Bitmap bmp_enemyPlane2_injured;//敌机2受伤的图片
    public static Bitmap bmp_enemyPlane3_injured;//敌机3受伤的图片
    static Bitmap[] bmps_enemyPlane1 = new Bitmap[1];//敌机1飞行的数组
    static Bitmap[] bmps_enemyPlane2 = new Bitmap[1];//敌机2飞行的数组
    static Bitmap[] bmps_enemyPlane3 = new Bitmap[2];//敌机3飞行的数组
    static Bitmap[] bmps_hero_explode = new Bitmap[4];//英雄飞机爆炸的数组
    static Bitmap bmp_changeBullet;//子弹补给的图片
    static Bitmap bmp_Bomb;//炸弹补给的图片
    static Bitmap bmp_bullet1;
    static Bitmap bmp_bullet2;
    static Bitmap[] bmps_heroPlane = new Bitmap[2];//英雄飞机飞行的数组
    public ArrayList<Explode> explodeList = new ArrayList<>();//爆炸
    public ChangeBullet changeBullet;//子弹补给
    public Bomb bomb;//炸弹补给
    public int bg1y, bg2y;//背景图的两个y坐标
    public HeroPlane heroPlane;
    public MoveThread moveThread;//移动物体的线程
    public RunThread runThread;//飞机飞行动画的线程
    public ExplodeThread explodeThread;//爆炸换帧的线程
    public TutorialThread thread;//刷帧的线程
    public int mEnemyId = 0; //初始化敌军ID
    public Long mSendTime = 0L; //上一颗子弹发射的时间
    public Long mEnemyTime = 0L; //上一次敌军出现的时间
    public Long mTime = 0L; //计算敌机出现,,用的基准时间
    public Long mDifficultyTime = 0L; //计算难度用的时间
    public Long mBombTime = 0L; //计算炸弹补给出现的时间
    public Long mChangeBulletTime = 0L; //计算子弹补给出现的时间
    public CopyOnWriteArrayList<Bullet> mBullets = null;//子弹数组 使用此数组可避免ArrayList在迭代时remove导致的异常
    public EnemyPlane mEnemy[] = null;//敌军飞机数组
    GamePlaying activity;
    Bitmap background;//背景的大图元
    int[] IDs_heroPlane_explode;//英雄飞机爆炸的图片资源ID数组
    int[] IDs_enemyPlane1_explode;//敌机1爆炸的图片资源ID数组
    int[] IDs_enemyPlane2_explode;//敌机2爆炸的图片资源ID数组
    int[] IDs_enemyPlane3_explode;//敌机3爆炸的图片资源ID数组
    Paint paint;
    SoundPool soundPool;//声音
    HashMap<Integer, Integer> soundPoolMap;


    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs,
                    int defStyle) {
        super(context, attrs, defStyle);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (GamePlaying) context;
        this.setKeepScreenOn(true);//保持屏幕常亮
        this.setFocusableInTouchMode(false);//设置不允许按键
        Display display = activity.getWindowManager().getDefaultDisplay();//获取屏幕宽高
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
        initBitmap();
        initSounds();
        initGameView();

    }


    public void initGameView() {

        getHolder().addCallback(this);//注册接口

        this.thread = new TutorialThread(getHolder(), this);//初始化刷帧线程
        this.runThread = new RunThread(this);//初始化飞行动画线程
        this.moveThread = new MoveThread(this);//初始化物品及敌机飞行线程
        this.explodeThread = new ExplodeThread(this);//初始化爆炸线程

        heroPlane = new HeroPlane((screenWidth - bmps_heroPlane[0].getWidth()) / 2, screenHeight / 3 * 2 - bmps_heroPlane[0].getHeight() / 2);//初始化我方飞机
//        mBullet = new Bullet[ConstantUtil.BULLET_POOL_COUNT];
        mBullets = new CopyOnWriteArrayList<>();
//        for (int i = 0; i < ConstantUtil.BULLET_POOL_COUNT; i++) {
//            mBullet[i] = new Bullet(0, 0);
//        }
        mEnemy = new EnemyPlane[ConstantUtil.ENEMY_POOL_COUNT];
        for (int i = 0; i < ConstantUtil.ENEMY_POOL_COUNT; i++) {
            mEnemy[i] = new EnemyPlane(0, ConstantUtil.ENEMY_TYPE1);
        }
        mTime = System.currentTimeMillis();
        mDifficultyTime = System.currentTimeMillis();
        mBombTime = System.currentTimeMillis();
        mChangeBulletTime = System.currentTimeMillis();
        changeBullet = new ChangeBullet(Map.getBullet());//取子弹补给
        bomb = new Bomb(Map.getBomb()); //取炸弹补给
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

    public void initBitmap() {//初始化所有图片

        paint = new Paint();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.game_background);//大背景图片
        background = Bitmap.createScaledBitmap(bmp, screenWidth, screenHeight, true);
        bg1y = Math.abs(background.getHeight() - screenHeight);//背景图的顶端y坐标
        bg2y = bg1y - background.getHeight();//

        bmps_heroPlane[0] = BitmapFactory.decodeResource(getResources(), R.drawable.playing_hero_run1);//英雄飞机的图片1
        bmps_heroPlane[1] = BitmapFactory.decodeResource(getResources(), R.drawable.playing_hero_run2);//英雄飞机的图片2
        bmps_enemyPlane1[0] = BitmapFactory.decodeResource(getResources(), R.drawable.playing_enemy1_run);//敌机1的图片
        bmps_enemyPlane2[0] = BitmapFactory.decodeResource(getResources(), R.drawable.playing_enemy2_run);//敌机2的图片
        bmps_enemyPlane3[0] = BitmapFactory.decodeResource(getResources(), R.drawable.playing_enemy3_run1);//敌机3的图片1
        bmps_enemyPlane3[1] = BitmapFactory.decodeResource(getResources(), R.drawable.playing_enemy3_run2);//敌机3的图片2

        bmp_Bomb = BitmapFactory.decodeResource(getResources(), R.drawable.playing_supply_bomb);
        bmp_changeBullet = BitmapFactory.decodeResource(getResources(), R.drawable.playing_supply_bullet);

        bmp_bullet1 = BitmapFactory.decodeResource(getResources(), R.drawable.playing_bullet_red);
        bmp_bullet2 = BitmapFactory.decodeResource(getResources(), R.drawable.playing_bullet_blue);

        bmp_enemyPlane2_injured = BitmapFactory.decodeResource(getResources(), R.drawable.playing_enemy2_injured);
        bmp_enemyPlane3_injured = BitmapFactory.decodeResource(getResources(), R.drawable.playing_enemy3_injured);

        IDs_heroPlane_explode = new int[]{//英雄飞机爆炸的所有帧
                R.drawable.playing_hero_dead1,
                R.drawable.playing_hero_dead2,
                R.drawable.playing_hero_dead3,
                R.drawable.playing_hero_dead4,
        };
        IDs_enemyPlane1_explode = new int[]{//敌机1爆炸的所有帧
                R.drawable.playing_enemy1_dead1,
                R.drawable.playing_enemy1_dead2,
                R.drawable.playing_enemy1_dead3,
                R.drawable.playing_enemy1_dead4,
        };
        IDs_enemyPlane2_explode = new int[]{//敌机2爆炸的所有帧
                R.drawable.playing_enemy2_dead1,
                R.drawable.playing_enemy2_dead2,
                R.drawable.playing_enemy2_dead3,
                R.drawable.playing_enemy2_dead4,
        };
        IDs_enemyPlane3_explode = new int[]{//敌机3爆炸的所有帧
                R.drawable.playing_enemy3_dead1,
                R.drawable.playing_enemy3_dead2,
                R.drawable.playing_enemy3_dead3,
                R.drawable.playing_enemy3_dead4,
                R.drawable.playing_enemy3_dead5,
                R.drawable.playing_enemy3_dead6,
        };
        for (int i = 0; i < bmps_hero_explode.length; i++) {//初始化英雄飞机爆炸图片
            bmps_hero_explode[i] = BitmapFactory.decodeResource(getResources(), IDs_heroPlane_explode[i]);
        }
        for (int i = 0; i < bmps_enemyPlane1_explode.length; i++) {//初始化敌机1爆炸图片
            bmps_enemyPlane1_explode[i] = BitmapFactory.decodeResource(getResources(), IDs_enemyPlane1_explode[i]);
        }
        for (int i = 0; i < bmps_enemyPlane2_explode.length; i++) {//初始化敌机2爆炸图片
            bmps_enemyPlane2_explode[i] = BitmapFactory.decodeResource(getResources(), IDs_enemyPlane2_explode[i]);
        }
        for (int i = 0; i < bmps_enemyPlane3_explode.length; i++) {//初始化敌机3爆炸图片
            bmps_enemyPlane3_explode[i] = BitmapFactory.decodeResource(getResources(), IDs_enemyPlane3_explode[i]);
        }

    }

    public void mDraw(final Canvas canvas) {

        if (canvas != null) {
            //绘制游戏背景
            canvas.drawBitmap(background, 0, bg1y, paint);
            canvas.drawBitmap(background, 0, bg2y, paint);

            //绘制玩家飞机
            if (heroPlane.getStatus()) {
                heroPlane.draw(canvas);
            }

            //计算当前时间
            long now = activity.getCurrentTime();

            //绘制子弹及敌机
            if (now - mTime >= ConstantUtil.FIRST_ENEMY_TIME) {
                drawEnemy(canvas, now);
                if (heroPlane.getStatus()) {
                    drawBullets(canvas, now);
                }
            }
            //绘制炸弹补给
            drawBomb(canvas, now);

            //绘制子弹补给
            drawChangeBullet(canvas, now);

            //计算难度
//            addDifficulty(now);

            //绘制爆炸
            try {
                for (Explode e : explodeList) {
                    e.draw(canvas);
                }

            } catch (Exception e) {

            }

        }


    }

    private void drawChangeBullet(Canvas canvas, long now) {
        if (changeBullet.getStatus()) {
            changeBullet.move();
        }
        //计算子弹补给时间
        if (now - mChangeBulletTime >= ConstantUtil.SUPPLY_BULLET_INTERVAL_TIME) {
            changeBullet.setStatus(true);
            mChangeBulletTime = now;
        }


        if (changeBullet.getStatus()) {
            changeBullet.draw(canvas);

        }
    }

    private void drawBomb(Canvas canvas, long now) {
        if (bomb.getStatus()) {
            bomb.move();
        }
        //计算炸弹补给时间
        if (now - mBombTime >= ConstantUtil.SUPPLY_BOMB_INTERVAL_TIME) {
            bomb.setStatus(true);
            mBombTime = now;
        }
        //绘制炸弹补给
        if (bomb.getStatus()) {
            bomb.draw(canvas);
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
        for (EnemyPlane ep : mEnemy) {
            ep.move();
        }


        //根据时间初始化敌军飞机
        if (mEnemyId < ConstantUtil.ENEMY_POOL_COUNT) {
            if (now - mEnemyTime >= ConstantUtil.ENEMY_TIME) {
                HashMap<String, Integer> map = Map.getEnemyPlan();
                mEnemy[mEnemyId].setX(map.get("Location"));
                mEnemy[mEnemyId].setY(-500);
                mEnemy[mEnemyId].setVelocity(map.get("Velocity"));
                mEnemy[mEnemyId].setType(map.get("Type"));

                mEnemyTime = now;
                mEnemy[mEnemyId].setStatus(true);
                mEnemyId++;
            }
        } else {
            mEnemyId = 0;
        }

        //绘制状态正常的敌军飞机
        for (EnemyPlane ep : mEnemy) {
            if (ep.getStatus()) {
                ep.draw(canvas);
            }
        }

    }

    private void drawBullets(Canvas canvas, long now) {


        //根据时间初始化未发射的子弹
        if (now - mSendTime >= ConstantUtil.BULLET_TIME) {
            int x = heroPlane.getX() + (int) ((bmps_heroPlane[0].getWidth() - bmp_bullet1.getWidth()) / 2.0);
            int y = heroPlane.getY() - ConstantUtil.BULLET_SPAN / 2;
            if (heroPlane.getBulletType() == ConstantUtil.BULLET_RED) {
                Bullet b1 = new Bullet(x, y);
                b1.setType(ConstantUtil.BULLET_RED);
                mBullets.add(b1);
            } else if (heroPlane.getBulletType() == ConstantUtil.BULLET_BLUE) {
                Bullet b1 = new Bullet(x, y - ConstantUtil.BULLET_SPAN);
                b1.setType(ConstantUtil.BULLET_BLUE);
                mBullets.add(b1);
                Bullet b2 = new Bullet(x - ConstantUtil.BULLET_SPAN, y);
                b2.setType(ConstantUtil.BULLET_BLUE);
                mBullets.add(b2);
                Bullet b3 = new Bullet(x + ConstantUtil.BULLET_SPAN, y);
                b3.setType(ConstantUtil.BULLET_BLUE);
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

                b.draw(canvas);
            }
        } catch (Exception e) {
            Logg.e(e);
        }

    }

    public void setBomb() {

        for (EnemyPlane ep : mEnemy) {
            if (ep.getStatus()) {
                ep.setStatus(false);
                Message msg = activity.myHandler.obtainMessage();
                switch (ep.getType()) {
                    case ConstantUtil.ENEMY_TYPE1:
                        playSound(2, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE1_SCORE;
                        break;
                    case ConstantUtil.ENEMY_TYPE2:
                        playSound(3, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE2_SCORE;
                        break;
                    case ConstantUtil.ENEMY_TYPE3:
                        playSound(4, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE3_SCORE;
                        break;
                }
                activity.myHandler.sendMessage(msg);
                Explode e = new Explode(ep.getX(), ep.getY(), ep.getType());
                explodeList.add(e);
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

            heroPlane.setX(Px - heroPlane.getBitmap().getWidth() / 2);
            heroPlane.setY(Py - heroPlane.getBitmap().getHeight());
        }

        return true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {//创建时启动相应进程

        this.thread.setFlag(true);//启动刷帧线程
        this.thread.start();

        this.runThread.setFlag(true);//启动飞行动画效果线程
        this.runThread.start();

        this.moveThread.setFlag(true);
        moveThread.start();//启动所有移动物的移动线程

        this.explodeThread.setFlag(true);
        explodeThread.start();//启动爆炸效果线程

    }

    public void surfaceDestroyed(SurfaceHolder holder) {//摧毁时释放相应进程
        boolean retry = true;
        this.thread.setFlag(false);
        this.moveThread.setFlag(false);
        this.explodeThread.setFlag(false);
        this.runThread.setFlag(false);

        soundPool.release();
        while (retry) {
            try {
                thread.join();
                moveThread.join();
                explodeThread.join();
                runThread.join();
                retry = false;
            } catch (InterruptedException e) {//不断地循环，直到刷帧线程结束
            }
        }
    }


}