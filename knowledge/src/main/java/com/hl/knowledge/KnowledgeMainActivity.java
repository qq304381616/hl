package com.hl.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hl.base.AppRouter;
import com.hl.base.BaseActivity;
import com.hl.knowledge.permission.PermissionActivity;
import com.hl.knowledge.touch.TouchActivity;

public class KnowledgeMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knowledge_activity_main);
        initToolbar(true);

        // kotlin
        findViewById(R.id.tv_kotlin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppRouter.turnPage(KnowledgeMainActivity.this, "com.hl.kotlin.KotlinMainActivity");
            }
        });

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
        // fragment
        findViewById(R.id.tv_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KnowledgeMainActivity.this, FragmentActivity.class);
                startActivity(intent);
            }
        });
        // activity
        findViewById(R.id.tv_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KnowledgeMainActivity.this, LaunchModeActivityA.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        // service
        findViewById(R.id.tv_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KnowledgeMainActivity.this, MyService.class);
                startService(intent);
            }
        });

        // 在两个ViewGroup里移动View
        findViewById(R.id.tv_move_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KnowledgeMainActivity.this, MoveViewActivity.class);
                startActivity(intent);
            }
        });
        // 监听屏幕外部滑入事件
        findViewById(R.id.tv_out_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KnowledgeMainActivity.this, OutMoveActivity.class);
                startActivity(intent);
            }
        });
    }
}