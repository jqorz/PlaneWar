package com.jqorz.planewar.constant;

import android.support.annotation.IntDef;

/**
 * @author j1997
 * @since 2020/4/19
 */
@IntDef({PlaneType.ENEMY_TYPE1, PlaneType.ENEMY_TYPE2, PlaneType.ENEMY_TYPE3})
public @interface PlaneType {
    int ENEMY_TYPE1 = 1;//敌军类型1
    int ENEMY_TYPE2 = 2;//敌军类型2
    int ENEMY_TYPE3 = 3;//敌军类型3
}
