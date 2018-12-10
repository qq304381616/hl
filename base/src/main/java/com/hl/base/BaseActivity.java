package com.hl.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hl.utils.ActivityUtils;
import com.hl.utils.L;

/**
 * activity base class
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        L.e("onConfigurationChanged");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("base onCreate");
        ActivityUtils.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("base onDestroy");
        ActivityUtils.getInstance().finishActivity(this);
    }

    private void initView() {
    }

    protected void onMenu() {
    }
}
