package com.jqorz.planewar.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;

import com.jqorz.planewar.anim.AnimationImpl;
import com.jqorz.planewar.base.BasePlane;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.tools.CollisionCheck;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 英雄飞机实体类
 */
public class HeroPlane extends BasePlane {

    private int bulletType;

    public HeroPlane() {
        super(BitmapLoader.bmps_heroPlane);
    }

    public int getBulletType() {
        return bulletType;
    }

    public void setBulletType(int bulletType) {
        this.bulletType = bulletType;
    }


    @Override
    public void init() {
        initLife();
        initAnimation();
        reset();
    }

    private void reset() {
        setBulletType(ConstantUtil.BULLET_RED);
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

    private void initAnimation() {
        mFlyAnimation = new AnimationImpl(getBitmaps(), true);
        mExploreAnimation = new AnimationImpl(BitmapLoader.bmps_hero_explode, false);
        mInjureAnimation = new AnimationImpl(getBitmaps(), false);
    }


    public void setX(int x) {
        this.x = Math.min(Math.max(0, x), GameView.screenWidth - getWidth());
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = Math.min(Math.max(0, y), GameView.screenHeight - getHeight());
    }


/*

    public void drawLife(Canvas canvas) {
        if (bmp_life != null) {
            for (int j = 0; j < ((5 - Hp) < 0 ? 5 : Hp); j++) {//绘制表示生命的小数条
                canvas.drawBitmap(bmp_life, 45 + bmp_life.getWidth() * j, 13, new Paint());
            }
        }
    }
*/


    //碰撞到子弹补给
    public boolean contain(ChangeBullet cb) {

        if (CollisionCheck.isCollision(this, cb)) {
            setBulletType(ConstantUtil.BULLET_BLUE);
            Thread rd = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(ConstantUtil.SUPPLY_BULLET_LONG_TIME);
                        setBulletType(ConstantUtil.BULLET_RED);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
            rd.start();

            return true;
        }
        return false;
    }

    //碰撞到炸弹补给
    public boolean contain(BombSupply b, GameView gameView) {
        Message msg = gameView.activity.myHandler.obtainMessage();
        if (CollisionCheck.isCollision(this, b)) {
            msg.arg2 = 1;
            gameView.activity.myHandler.sendMessage(msg);
            return true;
        }
        return false;
    }

    //碰撞到敌方飞机
    public boolean contain(EnemyPlane ep, GameView gameView) {

        if (CollisionCheck.isCollision(this, ep)) {

            this.life--;
            if (this.getLife() <= 0) {//当生命小于0时
                Explode e = new Explode(getX(), getY(), ConstantUtil.HERO_TYPE);
                gameView.explodeList.add(e);
                this.setStatus(ConstantUtil.STATUS_HIDE);//使自己不可见
                gameView.playSound(4, 0);
                gameView.activity.myHandler.sendEmptyMessage(ConstantUtil.STATE_END);//向主activity发送Handler消息
            }
            return true;
        }
        return false;
    }


    @Override
    public int getLife() {
        return life;
    }

    @Override
    protected void initLife() {
        this.life = ConstantUtil.Hero_life;
    }

    @Override
    public void move() {

    }
}


