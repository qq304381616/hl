package com.hl.skin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

/**
 * Created on 2017/3/14.
 */
public class SkinManager {

    private static SkinManager instance = new SkinManager();
    private Context context;
    private String skinPackage; // 插件的包名
    private Resources skinResource;

    public static SkinManager getInstance() {
        return instance;
    }

    public void init(Context c) {
        this.context = c;
    }

    public void loadSkin(String path) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        skinPackage = packageInfo.packageName;

        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //给AssetManager 赋值
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            Resources resource = context.getResources();

            skinResource = new Resources(assetManager, resource.getDisplayMetrics(), resource.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // resId = R.color.colorPrimary
    public int getColor(int resId) {
        if (skinResource == null) {
            return resId;
        }
        String resName = context.getResources().getResourceEntryName(resId);
        int trueResId = skinResource.getIdentifier(resName, "color", skinPackage);
        return skinResource.getColor(trueResId);
    }
}
