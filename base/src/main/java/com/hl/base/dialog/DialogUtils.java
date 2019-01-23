package com.hl.base.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

    public static Dialog createBaseDialog(Context c, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    public static Dialog getLoading(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("加载中...");
        return builder.create();
    }
}
