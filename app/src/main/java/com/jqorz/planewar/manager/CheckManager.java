package com.jqorz.planewar.manager;

import com.jqorz.planewar.entity.Bullet;
import com.jqorz.planewar.entity.EnemyPlane;
import com.jqorz.planewar.entity.GameView;
import com.jqorz.planewar.tools.MapCreator;
import com.jqorz.planewar.tools.TimeTools;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class CheckManager {
    public long mSendTime = 0L; //上一颗子弹发射的时间
    public long mEnemyTime = 0L; //上一次敌军出现的时间
    public long mTime = 0L; //计算敌机出现,,用的基准时间
    public long mDifficultyTime = 0L; //计算难度用的时间
    public long mBombTime = 0L; //计算炸弹补给出现的时间
    public long mChangeBulletTime = 0L; //计算子弹补给出现的时间
    private GameView gameView;
    private OnEntityChangeListener mOnEntityChangeListener;

    public CheckManager(GameView gameView) {//构造器
        this.gameView = gameView;
    }

    private void initTime() {
        long time = System.currentTimeMillis();
        mTime = time;
        mDifficultyTime = time;
        mBombTime = time;
        mChangeBulletTime = time;
    }

    public void checkNew() {
        if (mOnEntityChangeListener==null){
            return;
        }
        long now = TimeTools.getCurrentTime();

        //检查是否应该显示敌人
        if (now - mTime >= ConstantUtil.FIRST_ENEMY_TIME) {
            gameView.showEnemy = true;
        }

        //检查是否应该显示新敌人
        if (gameView.showEnemy && now - mEnemyTime >= ConstantUtil.ENEMY_TIME) {
            MapCreator.PlaneInfo info = MapCreator.getNewEnemyPlaneInfo();
            mOnEntityChangeListener.onNewEnemy(info);
            mEnemyTime = now;
        }

        //计算炸弹补给时间
        if (now - mBombTime >= ConstantUtil.SUPPLY_BOMB_INTERVAL_TIME) {
            mOnEntityChangeListener.onShowBombSupply();
            mBombTime = now;
        }

        //计算子弹补给时间
        if (now - mChangeBulletTime >= ConstantUtil.SUPPLY_BULLET_INTERVAL_TIME) {
            mOnEntityChangeListener.onShowBulletSupply();
            mChangeBulletTime = now;
        }

        //计算上次发送子弹的时间
        if (now - mSendTime >= ConstantUtil.BULLET_TIME) {
            for (MapCreator.BulletInfo info:MapCreator.getNewBulletPos(gameView.heroPlane)) {
                mOnEntityChangeListener.onNewBullet(info);
            }
            mSendTime = now;
        }
    }

    public void checkOutOfRange() {

    }

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

    public void setOnEntityChangeListener(OnEntityChangeListener onEntityChangeListener) {
        mOnEntityChangeListener = onEntityChangeListener;
    }

    public interface OnEntityChangeListener {
        void onNewEnemy(MapCreator.PlaneInfo planeInfo);

        void onNewBullet(MapCreator.BulletInfo bulletInfo);

        void onShowBulletSupply();

        void onShowBombSupply();
    }
}