package com.jqorz.planewar.tools;

import android.app.Application;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class DeviceTools {
    private static Application sApplication;
    private static int screenHeight = -1;
    private static int screenWidth = -1;

    public static void init(Application application) {
        sApplication = application;
    }

    public static int getScreenWidth() {
        if (screenWidth == -1) {
            screenWidth = sApplication.getResources().getDisplayMetrics().widthPixels;
        }
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenHeight == -1) {
            screenHeight = sApplication.getResources().getDisplayMetrics().heightPixels;
        }
        return screenHeight;
    }

    public static Application getApp() {
        return sApplication;
    }
}
