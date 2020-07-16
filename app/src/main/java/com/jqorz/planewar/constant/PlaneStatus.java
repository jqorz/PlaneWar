package com.jqorz.planewar.constant;

import android.support.annotation.IntDef;

/**
 * @author j1997
 * @since 2020/4/19
 */
@IntDef({PlaneStatus.STATUS_HIDE, PlaneStatus.STATUS_FLY, PlaneStatus.STATUS_EXPLORE, PlaneStatus.STATUS_INJURE})
public @interface PlaneStatus {
    int STATUS_HIDE = 0;//飞机默认状态
    int STATUS_FLY = 1;
    int STATUS_EXPLORE = 2;
    int STATUS_INJURE = 3;
}
