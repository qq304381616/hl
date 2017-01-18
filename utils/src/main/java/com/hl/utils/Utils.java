package com.hl.utils;

import android.os.Looper;

public class Utils {

    /**
     * 是否主线程
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
