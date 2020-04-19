package com.jqorz.planewar.entity;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.manager.MapManager;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为炸弹补给类
 */
public class BombSupply extends BaseSupply {


    BombSupply() {
        super(BitmapLoader.bmp_Bomb);
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