package com.hl.api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.utils.net.HttpCallback;
import com.hl.utils.net.NetUtils;

/**
 * 网络请求测试
 */
public class NetTestActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_nettest);
        findViewById(R.id.call).setOnClickListener(this);
        findViewById(R.id.call_async).setOnClickListener(this);
        TextView tv_result = findViewById(R.id.tv_result);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call) {
            NetUtils.weather("101010100", new HttpCallback() {
                @Override
                protected void onSuccess(Object dataEntity) {
                    super.onSuccess(dataEntity);

                }
            });
        } else if (v.getId() == R.id.call_async) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtils.syncWeather("101010100", new HttpCallback() {
                        @Override
                        protected void onSuccess(Object data) {
                            super.onSuccess(data);
                        }
                    });
                }
            }).start();
        }
    }
}
