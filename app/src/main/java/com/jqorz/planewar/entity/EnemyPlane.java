package com.jqorz.planewar.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.support.annotation.IntDef;

import com.jqorz.planewar.base.BasePlane;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为敌机类
 */
public class EnemyPlane extends BasePlane {

    private int x;
    private int y = -GameView.bmps_enemyPlane3[0].getHeight();
    private boolean status = true;//飞机的状态
    private int type;//敌机的类型
    private Bitmap bitmap;//当前帧
    private int velocity;//飞机的速度
    private int k = 0;//当前帧数
    private Bitmap[] bitmaps;//储存敌机飞行的图片数组

    public EnemyPlane(Bitmap[] bitmaps) {
        super(bitmaps);
        setType(type);
        reset();
    }

    public void reset() {
        setX(0);
        setY(-GameView.screenHeight);
    }


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(@PlaneType int type) {
        this.type = type;
        setBmps_Life(type);
    }

    public int getLife() {
        return life;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    private void setBmps_Life(int type) {
        switch (type) {
            case ConstantUtil.ENEMY_TYPE1:
                this.life = ConstantUtil.Enemy1_life;
                this.bitmaps = GameView.bmps_enemyPlane1;
                break;
            case ConstantUtil.ENEMY_TYPE2:
                this.life = ConstantUtil.Enemy2_life;
                this.bitmaps = GameView.bmps_enemyPlane2;
                break;
            case ConstantUtil.ENEMY_TYPE3:
                this.life = ConstantUtil.Enemy3_life;
                this.bitmaps = GameView.bmps_enemyPlane3;
                break;

        }
        bitmap = bitmaps[0];

    }

    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, x, y, new Paint());
        }
    }

    public void move() {
        this.y = this.y + velocity;
    }

    private boolean isContain(int otherX, int otherY, int otherWidth, int otherHeight) {//判断两个矩形是否碰撞
        int xd;//大的x
        int yd;//大大y
        int xx;//小的x
        int yx;//小的y
        int width;
        int height;
        boolean xFlag;//玩家飞机x是否在前
        boolean yFlag;//玩家飞机y是否在前
        if (this.x >= otherX) {
            xd = this.x;
            xx = otherX;
            xFlag = false;
        } else {
            xd = otherX;
            xx = this.x;
            xFlag = true;
        }
        if (this.y >= otherY) {
            yd = this.y;
            yx = otherY;
            yFlag = false;
        } else {
            yd = otherY;
            yx = this.y;
            yFlag = true;
        }
        if (xFlag) {
            width = this.bitmap.getWidth();
        } else {
            width = otherWidth;
        }
        if (yFlag) {
            height = this.bitmap.getHeight();
        } else {
            height = otherHeight;
        }
        if (xd >= xx && xd <= xx + width - 1 &&
                yd >= yx && yd <= yx + height - 1) {//首先判断两个矩形有否重叠
            double Dwidth = width - xd + xx;   //重叠区域宽度
            double Dheight = height - yd + yx; //重叠区域高度
            if (Dwidth * Dheight / (otherWidth * otherHeight) >= 0.20) {//重叠面积超20%则判定为碰撞
                return true;
            }
        }
        return false;
    }

    public boolean contain(Bullet b, GameView gameView) {//判断子弹是否打中敌机

        int bx = b.getX();
        int by = b.getY();
        int bw = b.getBitmap().getWidth();
        int bh = b.getBitmap().getHeight();
        /**
         * 废弃的根据类型碰撞检测的计算方法
         */
//        if (b.getType()==ConstantUtil.BULLET_BLUE){
//             bx=bx-ConstantUtil.BULLET_SPAN;
//             by=by-ConstantUtil.BULLET_SPAN;
//             bw=bw+ConstantUtil.BULLET_SPAN*2;
//             bh=bh+ConstantUtil.BULLET_SPAN;
//        }
        if (isContain(bx, by, bw, bh)) {
            switch (type) {
                case ConstantUtil.ENEMY_TYPE2:
                    bitmap = GameView.bmp_enemyPlane2_injured;
                    break;
                case 3:
                    bitmap = GameView.bmp_enemyPlane3_injured;
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
                Explode e = new Explode(getX(), getY(), getType());
                gameView.explodeList.add(e);
                setStatus(false);//使自己不可见
            }


            return true;
        }

        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {

        this.x = x;

    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void nextFrame() {//换帧
        if (k < bitmaps.length) {
            bitmap = bitmaps[k];
            k++;
        } else {
            k = 0;
            bitmap = bitmaps[k];
        }
    }

    @IntDef({ConstantUtil.ENEMY_TYPE1, ConstantUtil.ENEMY_TYPE2, ConstantUtil.ENEMY_TYPE3})
    public @interface PlaneType {
    }
}