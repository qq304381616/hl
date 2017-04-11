package com.hl.knowledge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hl.knowledge.touch.TouchActivity;

public class KnowledgeMainActivity extends Activity {

    private static final String LOG_TAG = KnowledgeMainActivity.class.getSimpleName();

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

    }
}