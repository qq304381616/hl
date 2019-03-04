package com.hl.base.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

    public static Dialog createBaseDialog(Context c, String title, String message, String s1, String s2, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(s1, listener);
        builder.setNegativeButton(s2, null);
        return builder.create();
    }

    public static Dialog createBaseDialog(Context c, String title, String message) {
        return createBaseDialog(c, title, message, "确认", "取消", null);
    }

    public static Dialog getLoading(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("加载中...");
        return builder.create();
    }
}
