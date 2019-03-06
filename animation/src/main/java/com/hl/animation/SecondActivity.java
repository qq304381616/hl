package com.hl.animation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hl.base.BaseActivity;

/**
 * 动画跳转页面
 */

public class SecondActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_ac_second);
    }

    public void onClick(View v) {
        finish();
    }
}
