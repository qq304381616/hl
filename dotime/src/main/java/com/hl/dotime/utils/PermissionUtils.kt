package com.hl.dotime.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.util.*

object PermissionUtils {

    /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    private fun checkPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检测多个权限
     *
     * @return 未授权的权限
     */
    private fun checkMorePermissions(context: Context, permissions: Array<String>): List<String> {
        val permissionList = ArrayList<String>()
        for (s in permissions) {
            if (!checkPermission(context, s)) permissionList.add(s)
        }
        return permissionList
    }

    /**
     * 请求多个权限
     */
    fun requestMorePermissions(context: Context, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(context as Activity, permissions, requestCode)
    }

    /**
     * 判断是否已拒绝过权限
     *
     * @return 如果应用之前请求过此权限但用户拒绝，此方法将返回 true;
     * -----------如果应用第一次请求权限或 用户在过去拒绝了权限请求，
     * -----------并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    private fun judgePermission(context: Context, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
    }

    /**
     * 判断权限是否申请成功
     */
    fun isPermissionRequestSuccess(grantResults: IntArray): Boolean {
        return grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 跳转到权限设置界面
     */
    @SuppressLint("ObsoleteSdkInt")
    fun toAppSetting(context: Context) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.action = Intent.ACTION_VIEW
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            intent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        context.startActivity(intent)
    }

    /**
     * 用户申请多个权限返回
     */
    fun onRequestMorePermissionsResult(context: Context, permissions: Array<String>, callback: PermissionCheckCallBack) {
        checkMorePermissions(context, permissions, callback)
    }

    /**
     * 检测多个权限
     * 具体实现由回调接口决定
     */
    fun checkMorePermissions(context: Context, permissions: Array<String>, callBack: PermissionCheckCallBack) {
        val permissionList = checkMorePermissions(context, permissions)
        if (permissionList.size == 0) {  // 用户已授予权限
            callBack.onHasPermission()
        } else {
            var isFirst = true
            for (i in permissionList.indices) {
                if (judgePermission(context, permissionList[i])) {
                    isFirst = false
                    break
                }
            }
            val unauthorizedMorePermissions = permissionList.toTypedArray()
            if (isFirst)
                callBack.onUserHasAlreadyTurnedDownAndDontAsk(*unauthorizedMorePermissions)
            else
                callBack.onUserHasAlreadyTurnedDown(*unauthorizedMorePermissions)
        }
    }

    /**
     * 检测并申请多个权限
     */
    fun checkAndRequestMorePermissions(context: Context, permissions: Array<String>, requestCode: Int, callBack: PermissionRequestSuccessCallBack?) {
        val permissionList = checkMorePermissions(context, permissions)
        if (permissionList.size == 0) {  // 用户已授予权限
            callBack?.onHasPermission()
        } else {
            requestMorePermissions(context, permissionList.toTypedArray(), requestCode)
        }
    }

    interface PermissionRequestSuccessCallBack {
        /**
         * 用户已授予权限
         */
        fun onHasPermission()
    }

    interface PermissionCheckCallBack {

        /**
         * 用户已授予权限
         */
        fun onHasPermission()

        /**
         * 用户已拒绝过权限
         *
         * @param permission:被拒绝的权限
         */
        fun onUserHasAlreadyTurnedDown(vararg permission: String)

        /**
         * 用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限;
         *
         * @param permission:被拒绝的权限
         */
        fun onUserHasAlreadyTurnedDownAndDontAsk(vararg permission: String)
    }
}
