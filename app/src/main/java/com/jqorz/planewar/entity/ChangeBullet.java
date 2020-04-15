package com.jqorz.planewar.entity;

import android.graphics.Bitmap;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.manager.MapManager;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为子弹补给类
 */
public class ChangeBullet extends BaseSupply {

    ChangeBullet(Bitmap defaultBitmap) {
        super(defaultBitmap);
    }

    @Override
    public void reset() {
        setX(MapManager.getNewSupplyPos(this));
        setY(-getHeight());
    }


    public void move() {
        y += ConstantUtil.ChangeBullet_Velocity;
    }
}