package com.jqorz.planewar.entity;

import android.graphics.Bitmap;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.manager.MapManager;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为炸弹补给类
 */
public class BombSupply extends BaseSupply {


    BombSupply(Bitmap defaultBitmap) {
        super(defaultBitmap);
    }

    @Override
    public void reset() {
        setX(MapManager.getNewSupplyPos(this));
        setY(-getHeight());
    }

    @Override
    public void move() {
        y += ConstantUtil.Bomb_Velocity;
    }

}