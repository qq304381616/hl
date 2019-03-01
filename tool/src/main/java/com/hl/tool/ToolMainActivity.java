package com.hl.tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.tool.color.TransparencyActivity;

public class ToolMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_activity_main);
        initToolbar(true);

        findViewById(R.id.tv_transparency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToolMainActivity.this, TransparencyActivity.class));
            }
        });
    }
}
