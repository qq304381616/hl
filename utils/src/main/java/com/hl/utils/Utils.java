package com.hl.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;

import java.io.File;

public class Utils {

    /**
     * 是否主线程
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 获取缓存目录
     */
    public static String getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }

    /**
     * 获取缓存目录
     */
    public static File getDiskCacheFile(Context context, String uniqueName) {
        return new File(getDiskCacheDir(context, uniqueName));
    }

    public static String getJsCachePath(Context context) {
        String result = getDiskCacheDir(context, "js");
        if (!new File(result).exists()) new File(result).mkdirs();
        return result;
    }

    /**
     * 转16进制字符串
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 取menifast 参数
     */
    public static String getAppKey(Context context, String key) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(key);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appKey;
    }
}
