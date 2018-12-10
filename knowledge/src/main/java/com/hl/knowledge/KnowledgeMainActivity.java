package com.hl.knowledge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.knowledge.permission.PermissionActivity;
import com.hl.knowledge.touch.TouchActivity;

public class KnowledgeMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knowledge_activity_main);

        // Touch事件分发
        findViewById(R.id.tv_touch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KnowledgeMainActivity.this, TouchActivity.class));
            }
        });

        // 6.0运行时权限
        findViewById(R.id.tv_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KnowledgeMainActivity.this, PermissionActivity.class));
            }
        });

        // 安卓Map集合
        findViewById(R.id.tv_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KnowledgeMainActivity.this, AndroidMapActivity.class));
            }
        });
    }
}