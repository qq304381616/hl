package com.hl.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * sdcard 工具类
 */
public class SDCardUtils {

    /**
     * 获取所有存储路径
     * SD卡 / 扩展卡内存
     */
    public static String[] getVolumePaths(Context context) {
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);
        try {
            Method mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            return (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡信息
     *
     * @return SDCardInfo
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getSDCardInfo() {
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        JSONObject object = new JSONObject();
        try {
            if (!isSDCardEnable()) return "";
            object.put("isExist", true);
            object.put("BlockSizeLong", sf.getBlockSizeLong()); // 基数
            object.put("BlockCountLong", sf.getBlockCountLong());
            object.put("TotalBytes", sf.getTotalBytes()); // = BlockCountLong * BlockSizeLong // 总空间。16G 显示 11.94G。因为不会算上系统空间
            object.put("FreeBlocksLong", sf.getFreeBlocksLong());
            object.put("FreeBytes", sf.getFreeBytes()); // = FreeBlocksLong * BlockSizeLong // 剩余总空间
            object.put("AvailableBlocksLong", sf.getAvailableBlocksLong());
            object.put("AvailableBytes", sf.getAvailableBytes()); // = AvailableBlocksLong * BlockSizeLong // 可用剩余空间
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
