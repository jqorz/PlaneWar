package com.jqorz.planewar.entity;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.manager.MapManager;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为子弹补给类
 */
public class ChangeBullet extends BaseSupply {

    ChangeBullet() {
        super(BitmapLoader.bmp_bullet1);
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