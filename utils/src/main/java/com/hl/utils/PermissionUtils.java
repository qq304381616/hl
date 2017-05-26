package com.hl.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * 6.0运行时权限
 *
 * 魅族权限问题，6.0 以下，用户拒绝权限申请导致崩溃。 使用try catch 处理
 */
public class PermissionUtils {

    private static final String LOG_TAG = "PermissionUtils";

    public static final int READ_CONTACTS = 1001;

    // 调用 check(activity,Manifest.permission.READ_CONTACTS, PermissionUtils.READ_CONTACTS);
    public static void check(Activity c, String premissions, int requestCode) {
        boolean b = ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        boolean b1 = ContextCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        boolean b2 = ContextCompat.checkSelfPermission(c, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        Log.e("LOG", "是否获取相应权限 : " + !b + " | " + !b1 + " | " + !b2);
        if (b || b1 || b2) {
            ArrayList<String> list = new ArrayList<String>();
            if (b || b2) list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (b2) list.add(Manifest.permission.CAMERA);
            String[] strings = new String[list.size()];
            list.toArray(strings);
            ActivityCompat.requestPermissions(c, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE}, READ_CONTACTS);
//            FragmentCompat.requestPermissions() // Fragment 使用v13包下面的 FragmentCompat
            boolean b3 = ActivityCompat.shouldShowRequestPermissionRationale(c, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean b4 = ActivityCompat.shouldShowRequestPermissionRationale(c, Manifest.permission.CAMERA);
            Log.e("LOG", "是否拒绝过权限申请 " + b3 + " | " + b4);
//            if (b3 || b4) T.showShort(getActivity(), "请在手机设置里开启权限");
        } else {
            // call
        }
    }

//    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS: {
                if (grantResults.length > 0) {
                    for (int s : grantResults) {
                        if (s != PackageManager.PERMISSION_GRANTED) {
                            Log.e("TAG", "已拒绝权限申请");
//                            T.showShort(getActivity(), "请在设置内开启权限");
                            return;
                        }
                    }
                    Log.e("TAG", "已同意权限申请");
                    // do
                } else {
                    Log.e("TAG", "已拒绝权限申请");
//                    T.showShort(this, "请开启权限");
                }
                return;
            }
        }
    }
}
