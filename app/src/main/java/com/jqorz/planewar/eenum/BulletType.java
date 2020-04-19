package com.jqorz.planewar.eenum;

import android.support.annotation.IntDef;

/**
 * @author j1997
 * @since 2020/4/19
 */
@IntDef({BulletType.BULLET_RED, BulletType.BULLET_BLUE})
public @interface BulletType {
    int BULLET_RED = 1;//子弹1
    int BULLET_BLUE = 2;//子弹2
}
