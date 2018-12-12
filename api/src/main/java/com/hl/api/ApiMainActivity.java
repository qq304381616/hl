package com.hl.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hl.api.jpush.MainActivity;
import com.hl.api.receiver.ReceiverActivity;
import com.hl.api.thread.ThreadActivity;
import com.hl.api.zxing.activity.CaptureActivity;
import com.hl.base.BaseActivity;
import com.hl.utils.L;

/**
 * Created on 2017/4/6.
 */
public class ApiMainActivity extends BaseActivity {

    private final static int GO_SCAN_RESULT_CODE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_main);
        initToolbar(true);

        findViewById(R.id.tv_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ApiMainActivity.this, CaptureActivity.class), GO_SCAN_RESULT_CODE);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GO_SCAN_RESULT_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    final String result = data.getExtras().getString("result");
                    L.e("扫一扫结果：" + result);
            }
        }
    }
}
