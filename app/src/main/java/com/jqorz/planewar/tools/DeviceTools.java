package com.jqorz.planewar.tools;

import android.app.Application;
import android.util.DisplayMetrics;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class DeviceTools {
    private static Application sApplication;

    public static void init(Application application) {
        sApplication = application;
    }

    public static int getScreenWidth() {
        if (sApplication != null) {
            DisplayMetrics metrics = sApplication.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        }
        return 0;
    }

    public static int getScreenHeight() {
        if (sApplication != null) {
            DisplayMetrics metrics = sApplication.getResources().getDisplayMetrics();
            return metrics.heightPixels;
        }
        return 0;
    }
}
