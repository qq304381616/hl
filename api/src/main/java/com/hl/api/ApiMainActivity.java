package com.hl.api;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hl.api.jpush.MainActivity;
import com.hl.api.receiver.ReceiverActivity;
import com.hl.api.thread.ThreadActivity;
import com.hl.base.BaseActivity;

/**
 * Created on 2017/4/6.
 */
public class ApiMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_main);

        findViewById(R.id.tv_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApiMainActivity.this, ThreadActivity.class));
            }
        });

        findViewById(R.id.tv_jpush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApiMainActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.tv_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApiMainActivity.this, ReceiverActivity.class));
            }
        });
    }
}
