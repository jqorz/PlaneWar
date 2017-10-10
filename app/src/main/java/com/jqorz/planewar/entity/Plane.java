package com.jqorz.planewar.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;

import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 英雄飞机实体类
 */
public class Plane {

    private int x, y;//飞机位置(x,y)
    private Bitmap[] bitmaps = new Bitmap[2];
    private int Hp = ConstantUtil.life;//飞机生命值
    private int k = 0;//当前帧
    private Bitmap bitmap;//当前帧
    private boolean status = true;//标志自己是否可见
    private int bulletType = ConstantUtil.BULLET_RED;

    public Plane(int x, int y) {
        this.x = x;
        this.y = y;
        this.bitmaps = GameView.bmps_heroPlane;

    }

    public int getBulletType() {
        return bulletType;
    }

    public void setBulletType(int bulletType) {
        this.bulletType = bulletType;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        if (this.x < 0) {
            this.x = 0;
        }
        if (this.x > GameView.screenWidth - bitmap.getWidth()) {
            this.x = GameView.screenWidth - bitmap.getWidth();
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        if (y < 0) {
            this.y = 0;
        }
        if (this.y > GameView.screenHeight - bitmap.getHeight()) {
            this.y = GameView.screenHeight - bitmap.getHeight();
        }
    }

    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, x, y, new Paint());
        }
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

    public void nextFrame() {//换帧，成功返回true。否则返回false

        if (k < bitmaps.length) {
            bitmap = bitmaps[k];
            k++;
        } else {
            k = 0;
            bitmap = bitmaps[k];
        }
    }

    //碰撞到子弹补给
    public boolean contain(ChangeBullet cb) {

        if (isContain(cb.getX(), cb.getY(), cb.getBitmap().getWidth(), cb.getBitmap().getHeight())) {//检测成功
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
    public boolean contain(Bomb b, GameView gameView) {
        Message msg = gameView.activity.myHandler.obtainMessage();
        if (isContain(b.getX(), b.getY(), b.getBitmap().getWidth(), b.getBitmap().getHeight())) {//检测成功
            msg.arg2 = 1;
            gameView.activity.myHandler.sendMessage(msg);
            return true;
        }
        return false;
    }

    //碰撞到敌方飞机
    public boolean contain(EnemyPlane ep, GameView gameView) {

        if (isContain(ep.getX(), ep.getY(), ep.getBitmap().getWidth(), ep.getBitmap().getHeight())) {//检测成功

            this.Hp--;
            if (this.Hp <= 0) {//当生命小于0时
                Explode e = new Explode(getX(), getY(), ConstantUtil.HERO_TYPE);
                gameView.explodeList.add(e);
                this.setStatus(false);//使自己不可见
                gameView.playSound(4, 0);
                gameView.activity.myHandler.sendEmptyMessage(ConstantUtil.STATE_END);//向主activity发送Handler消息
            }
            return true;
        }
        return false;
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
}


