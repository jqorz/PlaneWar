package com.jqorz.planewar.tools;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class TimeTools {
    private static long pauseTime = 0L;//记录暂停时间
    private static long resumeTime = 0L;//记录恢复时间

    public static void setPauseTime() {
        TimeTools.pauseTime = System.currentTimeMillis();
    }

    public static void setResumeTime() {
        TimeTools.resumeTime = System.currentTimeMillis();
    }

    public static long getCurrentTime() {
        long now = System.currentTimeMillis();
        if (resumeTime - pauseTime >= 0) {
            return now - (resumeTime - pauseTime);
        } else {
            return now;
        }
    }
}
