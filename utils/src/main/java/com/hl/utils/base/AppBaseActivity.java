package com.hl.utils.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hl.utils.ActivityUtils;
import com.hl.utils.UmengUtils;

public class AppBaseActivity extends FragmentActivity {

    private static final String TAG = AppBaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().addActivity(this);
    }

    /**
     * 避免Activity被系统回收丢失数据
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.activityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.activityPause();
    }

    @Override
    protected void onDestroy() {
        ActivityUtils.getInstance().finishActivity(this);
        super.onDestroy();
    }
}