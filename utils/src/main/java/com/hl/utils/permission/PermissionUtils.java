package com.hl.utils.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.0运行时权限
 * 魅族权限问题，6.0 以下，用户拒绝权限申请导致崩溃。 使用try catch 处理
 */
public class PermissionUtils {

    /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    private static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检测多个权限
     *
     * @return 未授权的权限
     */
    private static List<String> checkMorePermissions(Context context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String s : permissions) {
            if (!checkPermission(context, s)) permissionList.add(s);
        }
        return permissionList;
    }

    /**
     * 请求多个权限
     */
    public static void requestMorePermissions(Context context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }

    /**
     * 判断是否已拒绝过权限
     *
     * @return 如果应用之前请求过此权限但用户拒绝，此方法将返回 true;
     * -----------如果应用第一次请求权限或 用户在过去拒绝了权限请求，
     * -----------并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    private static boolean judgePermission(Context context, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
    }

    /**
     * 判断权限是否申请成功
     */
    public static boolean isPermissionRequestSuccess(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 跳转到权限设置界面
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void toAppSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    /**
     * 用户申请多个权限返回
     */
    public static void onRequestMorePermissionsResult(Context context, String[] permissions, PermissionCheckCallBack callback) {
        checkMorePermissions(context, permissions, callback);
    }

    /**
     * 检测多个权限
     * 具体实现由回调接口决定
     */
    public static void checkMorePermissions(Context context, String[] permissions, PermissionCheckCallBack callBack) {
        List<String> permissionList = checkMorePermissions(context, permissions);
        if (permissionList.size() == 0) {  // 用户已授予权限
            callBack.onHasPermission();
        } else {
            boolean isFirst = true;
            for (int i = 0; i < permissionList.size(); i++) {
                if (judgePermission(context, permissionList.get(i))) {
                    isFirst = false;
                    break;
                }
            }
            String[] unauthorizedMorePermissions = permissionList.toArray(new String[permissionList.size()]);
            if (isFirst)
                callBack.onUserHasAlreadyTurnedDownAndDontAsk(unauthorizedMorePermissions);
            else
                callBack.onUserHasAlreadyTurnedDown(unauthorizedMorePermissions);
        }
    }

    /**
     * 检测并申请多个权限
     */
    public static void checkAndRequestMorePermissions(Context context, String[] permissions, int requestCode, PermissionRequestSuccessCallBack callBack) {
        List<String> permissionList = checkMorePermissions(context, permissions);
        if (permissionList.size() == 0) {  // 用户已授予权限
            if (callBack != null) {
                callBack.onHasPermission();
            }
        } else {
            requestMorePermissions(context, permissionList.toArray(new String[permissionList.size()]), requestCode);
        }
    }

    public interface PermissionRequestSuccessCallBack {
        /**
         * 用户已授予权限
         */
        void onHasPermission();
    }

    public interface PermissionCheckCallBack {

        /**
         * 用户已授予权限
         */
        void onHasPermission();

        /**
         * 用户已拒绝过权限
         *
         * @param permission:被拒绝的权限
         */
        void onUserHasAlreadyTurnedDown(String... permission);

        /**
         * 用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限;
         *
         * @param permission:被拒绝的权限
         */
        void onUserHasAlreadyTurnedDownAndDontAsk(String... permission);
    }
}
