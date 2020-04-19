package com.jqorz.planewar.eenum;

import android.support.annotation.IntDef;

import com.jqorz.planewar.utils.ConstantUtil;

/**
 * @author j1997
 * @since 2020/4/19
 */
@IntDef({ConstantUtil.STATUS_HIDE, ConstantUtil.STATUS_FLY, ConstantUtil.STATUS_EXPLORE, ConstantUtil.STATUS_INJURE})
public @interface PlaneStatus {
}
