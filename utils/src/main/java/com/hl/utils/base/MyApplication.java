package com.hl.utils.base;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MyApplication extends Application {

    private static Context mContext;
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mRefWatcher = LeakCanary.install(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
