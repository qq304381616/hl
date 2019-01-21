package com.hl.api;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.hl.api.jpush.MainActivity;
import com.hl.api.receiver.ReceiverActivity;
import com.hl.api.thread.ThreadActivity;
import com.hl.api.zxing.activity.CaptureActivity;
import com.hl.api.zxing.activity.ResultActivity;
import com.hl.base.BaseActivity;
import com.hl.base.dialog.DialogUtils;
import com.hl.utils.L;
import com.hl.utils.permission.PermissionUtils;

import java.util.Arrays;

/**
 * Created on 2017/4/6.
 * 第三方库
 */
public class ApiMainActivity extends BaseActivity {

    private final static int GO_SCAN_RESULT_CODE = 1001;
    private final static int CAMERA_PERMISSION_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_main);
        initToolbar(true);

        findViewById(R.id.tv_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.checkAndRequestMorePermissions(ApiMainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE,
                        new PermissionUtils.PermissionRequestSuccessCallBack() {
                            @Override
                            public void onHasPermission() {
                                // 权限已被授予
                                toCamera();
                            }
                        });
            }
        });

        findViewById(R.id.tv_make_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApiMainActivity.this, ResultActivity.class));

            }
        });
        findViewById(R.id.tv_eventbus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApiMainActivity.this, EventBusActivity.class));
            }
        });

        findViewById(R.id.tv_pinyin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApiMainActivity.this, PinyinActivity.class));
            }
        });

        findViewById(R.id.tv_net).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApiMainActivity.this, NetTestActivity.class));
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

        findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApiMainActivity.this, PhotoActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GO_SCAN_RESULT_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        final String result = bundle.getString("result");
                        L.e("扫一扫结果：" + result);
                        DialogUtils.createBaseDialog(ApiMainActivity.this, "", result).show();
                    }
            }
        }
    }

    private void toCamera() {
        startActivityForResult(new Intent(ApiMainActivity.this, CaptureActivity.class), GO_SCAN_RESULT_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                PermissionUtils.onRequestMorePermissionsResult(this, permissions, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        toCamera();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        Toast.makeText(getApplication(), "我们需要" + Arrays.toString(permission) + "权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        Toast.makeText(getApplication(), "我们需要" + Arrays.toString(permission) + "权限", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}
