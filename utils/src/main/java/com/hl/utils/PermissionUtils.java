package com.hl.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * 6.0运行时权限
 */
public class PermissionUtils {

    private static final String LOG_TAG = "PermissionUtils";

    public static void check(Activity c) {
        boolean b = ContextCompat.checkSelfPermission(c, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED;
        Log.e(LOG_TAG  , "未获取相应权限 " + b);
        if (b) {

            boolean b1 = ActivityCompat.shouldShowRequestPermissionRationale(c,
                    Manifest.permission.READ_CONTACTS);
            Log.e(LOG_TAG  , "是否拒绝过权限申请" + b1);
            // Should we show an explanation?
            if (b1) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(c,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1001);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            //
        }
    }

    public void request(Activity c) {
        ActivityCompat.requestPermissions(c,
                new String[]{Manifest.permission.READ_CONTACTS},
                1001);
    }

    /**
     * 回调
     */
//    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
