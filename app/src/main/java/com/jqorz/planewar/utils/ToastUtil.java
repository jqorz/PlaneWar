package com.jqorz.planewar.utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.jqorz.planewar.tools.DeviceTools;

/**
 * Toast工具类
 */
public class ToastUtil {


    private static Toast mToast = null;

    @SuppressLint("ShowToast")
    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(DeviceTools.getApp(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


}
