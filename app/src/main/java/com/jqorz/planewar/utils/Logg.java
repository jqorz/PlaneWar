package com.jqorz.planewar.utils;

import android.util.Log;

/**
 * Log管理的工具类
 */
public class Logg {

    public static boolean isDebug = true;


    public static void e(String tag, Object msg) {
        Log.e(tag, msg.toString());
    }

    public static void e(Object msg) {
        Log.e("-----------------------", msg.toString() + "-----------------------");
    }

    public static void i(String tag, Object msg) {
        if (isDebug) {
            Log.i(tag, msg.toString());
        }
    }

    public static void i(Object msg) {
        if (isDebug) {
            Log.i("-----------------------", msg.toString() + "-----------------------");
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg + "");
        }
    }

}
