package com.hl.base.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.base.R;

/**
 * 测试页
 */
public class TestActivity extends BaseActivity {

    public static Intent getIntent(Context c, String message) {
        Intent intent = new Intent(c, TestActivity.class);
        intent.putExtra("msg", message);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_test);
        initToolbar(true);

        TextView tv_message = findViewById(R.id.tv_message);
        tv_message.setText(getIntent().getStringExtra("msg"));
    }
}
