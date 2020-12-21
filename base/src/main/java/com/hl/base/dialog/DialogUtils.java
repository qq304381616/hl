package com.hl.base.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

public class DialogUtils {

    // loading
    public static Dialog getLoading(Context c) {
        return getLoading(c, "加载中..");
    }

    // loading
    public static Dialog getLoading(Context c, String info) {
        return getLoading(c, info, true);
    }

    // loading
    public static Dialog getLoading(Context c, String info, boolean cancelable) {
        ProgressDialog waitingDialog = new ProgressDialog(c);
        waitingDialog.setMessage(info);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(cancelable);
        return waitingDialog;
    }

    // dialog
    public static Dialog getDialog(Context c, String title, String positive, String negative,
                                   AlertDialog.OnClickListener pListener, AlertDialog.OnClickListener nListener) {
        return getDialog(c, title, null, positive, negative, pListener, nListener);
    }

    // dialog
    public static Dialog getDialog(Context c, String title, String message, String positive, String negative,
                                   AlertDialog.OnClickListener pListener, AlertDialog.OnClickListener nListener) {
        return getDialog(c, title, message, null, positive, negative, pListener, nListener);
    }

    // dialog
    public static Dialog getDialog(Context c, String title, String message, View view, String positive, String negative,
                                   AlertDialog.OnClickListener pListener, AlertDialog.OnClickListener nListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title);
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (view != null) {
            builder.setView(view);
        }
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, pListener);
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, nListener);
        }
        return builder.create();
    }
}
