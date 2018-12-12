package com.hl.utils.net;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created on 2017/5/26.
 */
public class LoadingViewImpl implements ILoadingView {

    private Dialog dialog;

    @Override
    public Context getContext() {
        return null;  // TODO
    }

    public void showLoading() {
        dialog = getDialog(getContext(), "");
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void hideLoading() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public static Dialog getDialog(Context c, String info) {
        ProgressDialog waitingDialog = new ProgressDialog(c);
        waitingDialog.setMessage(info);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        return waitingDialog;
    }
}
