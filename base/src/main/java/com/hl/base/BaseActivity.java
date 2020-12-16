package com.hl.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hl.utils.ActivityUtils;
import com.hl.utils.L;

/**
 * activity base class
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /* manifest android:configChanges="keyboardHidden|orientation|screenSize"
         * 配置前 旋转屏幕会执行 onCreate onDestroy, 并刷新布局。
         * 配置后 旋转屏幕 会执行此方法
         */
        L.e("onConfigurationChanged");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("base onCreate");
        ActivityUtils.getInstance().addActivity(this);
    }

    protected void initToolbar(boolean showBack) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (showBack && getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        }
    }

    protected void setTitleText(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return onBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!onBack()) {
            super.onBackPressed();
        }
    }

    protected boolean onBack() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("base onDestroy");
        ActivityUtils.getInstance().removeActivity(this);
    }
}
