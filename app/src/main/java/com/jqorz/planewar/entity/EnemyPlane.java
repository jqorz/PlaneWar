package com.jqorz.planewar.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.support.annotation.IntDef;
import android.transition.Explode;

import com.jqorz.planewar.anim.AnimationImpl;
import com.jqorz.planewar.base.BasePlane;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.tools.CollisionCheck;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为敌机类
 */
public class EnemyPlane extends BasePlane {

    private int type;//敌机的类型
    private int velocity;//飞机的速度

    public EnemyPlane(@PlaneType int type) {
        super(type == ConstantUtil.ENEMY_TYPE1 ? BitmapLoader.bmps_enemyPlane1 : type == ConstantUtil.ENEMY_TYPE2 ? BitmapLoader.bmps_enemyPlane2 : BitmapLoader.bmps_enemyPlane3);
        setType(type);
    }

    @Override
    public void init() {
        initLife();
        initAnimation();
        reset();
    }

    private void initAnimation() {
        mFlyAnimation = new AnimationImpl(getBitmaps(), true);
        switch (type) {
            case ConstantUtil.ENEMY_TYPE1:
                mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_enemyPlane1_explode, false);
                mInjureAnimation = new AnimationImpl(getBitmaps(), false);
                break;
            case ConstantUtil.ENEMY_TYPE2:
                mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_enemyPlane2_explode, false);
                mInjureAnimation = new AnimationImpl(BitmapLoader.bmp_enemyPlane2_injured, false);
                break;
            case ConstantUtil.ENEMY_TYPE3:
                mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_enemyPlane3_explode, false);
                mInjureAnimation = new AnimationImpl(BitmapLoader.bmp_enemyPlane3_injured, false);
                break;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        switch (status) {
            case ConstantUtil.STATUS_FLY:
                mFlyAnimation.drawFrame(canvas, paint, x, y, mFlyFrameId);
                break;
            case ConstantUtil.STATUS_EXPLORE:
                mExploreAnimation.drawFrame(canvas, paint, x, y, mExploreFrameId);
                break;
            case ConstantUtil.STATUS_INJURE:
                mInjureAnimation.drawFrame(canvas, paint, x, y, mInjureFrameId);
                break;
        }
    }

    public void reset() {
        setX(0);
        setY(-getHeight());
        setStatus(ConstantUtil.STATUS_HIDE);
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLife() {
        return life;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    protected void initLife() {
        switch (type) {
            case ConstantUtil.ENEMY_TYPE1:
                this.life = ConstantUtil.Enemy1_life;
                break;
            case ConstantUtil.ENEMY_TYPE2:
                this.life = ConstantUtil.Enemy2_life;
                break;
            case ConstantUtil.ENEMY_TYPE3:
                this.life = ConstantUtil.Enemy3_life;
                break;
        }
    }


    public void move() {
        this.y = this.y + velocity;
    }


    public boolean contain(Bullet b, GameView gameView) {//判断子弹是否打中敌机


        if (CollisionCheck.isCollision(this, b)) {
            switch (type) {
                case ConstantUtil.ENEMY_TYPE2:
                    setStatus(ConstantUtil.STATUS_INJURE);
                    break;
                case ConstantUtil.ENEMY_TYPE3:
                    setStatus(ConstantUtil.STATUS_INJURE);
                    break;
            }
            gameView.playSound(2, 0);//播放受到攻击音效
            this.life--;//自己的生命减1
            if (getLife() <= 0) {//当生命小于0时，向主线程发送得分信息
                Message msg = gameView.activity.myHandler.obtainMessage();
                switch (getType()) {
                    case ConstantUtil.ENEMY_TYPE1:
                        gameView.playSound(2, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE1_SCORE;
                        break;
                    case ConstantUtil.ENEMY_TYPE2:
                        gameView.playSound(3, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE2_SCORE;
                        break;
                    case ConstantUtil.ENEMY_TYPE3:
                        gameView.playSound(4, 0);
                        msg.arg1 = ConstantUtil.ENEMY_TYPE3_SCORE;
                        break;
                }
                gameView.activity.myHandler.sendMessage(msg);
                setStatus(ConstantUtil.STATUS_EXPLORE);
            }


            return true;
        }

        return false;
    }


    @IntDef({ConstantUtil.ENEMY_TYPE1, ConstantUtil.ENEMY_TYPE2, ConstantUtil.ENEMY_TYPE3})
    public @interface PlaneType {
    }
}