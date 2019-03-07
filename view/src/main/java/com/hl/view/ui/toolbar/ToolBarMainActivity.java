package com.hl.view.ui.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.view.R;

public class ToolBarMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_toolbar_main);
        initToolbar(true);

        findViewById(R.id.tv_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToolBarMainActivity.this, ToolBarActivity.class));
            }
        });
        findViewById(R.id.tv_toolbar2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToolBarMainActivity.this, ToolBarAppBarLayoutActivity.class));
            }
        });
        findViewById(R.id.tv_toolbar3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToolBarMainActivity.this, ToolBarCollapsingToolbarLayoutActivity.class));
            }
        });
    }
}
