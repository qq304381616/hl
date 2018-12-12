package com.hl.base;

import android.app.Application;

import com.hl.utils.CrashUtils;
import com.squareup.leakcanary.LeakCanary;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LeakCanary.install(this);
        CrashUtils.getInstance().init(this);
    }
}
