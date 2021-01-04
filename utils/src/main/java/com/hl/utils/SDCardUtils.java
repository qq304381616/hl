package com.hl.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.MediaStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
     * 获取存储盘 名称
     * getExternalVolumeNames() 是 Q 及以上版本调用。
     * <p>
     * 样式: content://media/external_primary/images/media
     * 固定写法: MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
     */
    @TargetApi(29)
    public static List<String> getVolume(Context context) {
        List<String> result = new ArrayList<>();
        // 显示公共所有存储设备， 及对应URI
        for (String volumeName : MediaStore.getExternalVolumeNames(context)) {
            result.add(volumeName);
        }
        return result;
    }

    /**
     * 获取公共目录 URI，
     *
     * @param type   Images Audio Video Files Downloads
     * @param volume 指定存储卷。Files 必须指定。
     */
    public static Uri getUri(String type, String volume) {
        if (type.equals("Images")) {
            if (volume.equals("internal")) {
                return MediaStore.Images.Media.INTERNAL_CONTENT_URI;
            } else if (volume.equals("external")) {
                return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else {
                return MediaStore.Images.Media.getContentUri(volume);
            }
        } else if (type.equals("Audio")) {
            if (volume.equals("internal")) {
                return MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            } else if (volume.equals("external")) {
                return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            } else {
                return MediaStore.Audio.Media.getContentUri(volume);
            }
        } else if (type.equals("Video")) {
            if (volume.equals("internal")) {
                return MediaStore.Video.Media.INTERNAL_CONTENT_URI;
            } else if (volume.equals("external")) {
                return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else {
                return MediaStore.Video.Media.getContentUri(volume);
            }
        } else if (type.equals("File")) {
            return MediaStore.Files.getContentUri(volume);
        } else if (type.equals("Downloads")) {
            if (volume.equals("internal")) {
                return MediaStore.Downloads.INTERNAL_CONTENT_URI;
            }else if (volume.equals("external")) {
                return MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            }else {
                return MediaStore.Downloads.getContentUri(volume);
            }
        }
        return null;
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
