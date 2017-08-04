package com.hl.utils;

import android.content.Context;
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
}
