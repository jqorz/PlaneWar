package com.jqorz.planewar.entity;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.tools.MapCreator;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为炸弹补给类
 */
public class BombSupply extends BaseSupply {


    BombSupply() {
        super(BitmapLoader.bmp_bombSupply);
    }

    @Override
    public void reset() {
        setX(MapCreator.getNewSupplyPos(this));
        setY(-getHeight());
        setShown(true);
    }

    @Override
    public void move() {
        y += ConstantUtil.Bomb_Velocity;
    }

}