package com.jqorz.planewar.tools;

import com.jqorz.planewar.utils.UserDataUtil;

/**
 * @author j1997
 * @since 2020/5/4
 */
public class UserDataManager {
    public static boolean isOpenMusic() {
        return UserDataUtil.loadUserTrueBooleanData(DeviceTools.getApp(), "music");
    }

    public static void setOpenMusic(boolean openMusic) {
        UserDataUtil.updateUserData(DeviceTools.getApp(), "music", openMusic);
    }

    public static boolean isOpenSound() {
        return UserDataUtil.loadUserTrueBooleanData(DeviceTools.getApp(), "sound");
    }

    public static void setOpenSound(boolean openSound) {
        UserDataUtil.updateUserData(DeviceTools.getApp(), "sound", openSound);
    }
}
