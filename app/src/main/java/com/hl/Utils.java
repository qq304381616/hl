package com.hl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.util.List;

public class Utils {

    private static final String LOG_TAG = "AppsInfoListActivity";

    /**
     * 获取应用列表
     */
    public static List<PackageInfo> getInstalledApps(Context c){
        List<PackageInfo> packs = c.getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packs) {
            Log.e(LOG_TAG, "name : " + p.applicationInfo.loadLabel(c.getPackageManager())
                    .toString());
            Log.e(LOG_TAG, "package : " + p.packageName);
            Log.e(LOG_TAG, "versionName : " + p.versionName);
            Log.e(LOG_TAG, "versionCode : " + p.versionCode);
            Log.e(LOG_TAG, "icon : " + p.applicationInfo.loadIcon(c.getPackageManager()));
        }
    return packs;
    }
}
