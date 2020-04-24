package com.jqorz.planewar.entity;

import com.jqorz.planewar.base.BaseSupply;
import com.jqorz.planewar.tools.MapCreator;
import com.jqorz.planewar.tools.BitmapLoader;
import com.jqorz.planewar.utils.ConstantUtil;

/**
 * 该类为子弹补给类
 */
public class BulletSupply extends BaseSupply {

    BulletSupply() {
        super(BitmapLoader.bmp_bulletSupply);
        init();
    }

    @Override
    public void reset() {
        setX(MapCreator.getNewSupplyPos(this));
        setY(-getHeight());
    }


    public void move() {
        y += ConstantUtil.ChangeBullet_Velocity;
    }
}