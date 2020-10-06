package com.jqorz.planewar

import android.app.Application
import com.jqorz.planewar.tools.DeviceTools

/**
 * @author j1997
 * @since 2020/4/19
 */
class GameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DeviceTools.init(this)
    }
}