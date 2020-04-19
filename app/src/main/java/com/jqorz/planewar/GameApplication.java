package com.jqorz.planewar;

import android.app.Application;

import com.jqorz.planewar.tools.DeviceTools;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class GameApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DeviceTools.init(this);
    }

}
