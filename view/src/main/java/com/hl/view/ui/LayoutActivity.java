package com.hl.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.view.R;

/**
 * 布局控件
 */
public class LayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_layout);
        initToolbar(true);

        findViewById(R.id.tv_constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LayoutActivity.this, ConstraintLayoutActivity.class));
            }
        });

        findViewById(R.id.tv_nestedScrollView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LayoutActivity.this, NestedScrollViewActivity.class));
            }
        });
    }
}
