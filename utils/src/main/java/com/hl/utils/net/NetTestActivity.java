package com.hl.utils.net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hl.utils.R;

import java.io.IOException;

public class NetTestActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nettest);
        findViewById(R.id.call).setOnClickListener(this);
        tv_result = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call) {
            NetUtils.login("", new HttpCallback<DataEntity>() {
                @Override
                protected void onSuccess(DataEntity data) {
                    super.onSuccess(data);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        NetUtils.syncLogin("", new HttpCallback<DataEntity>() {
                            @Override
                            protected void onSuccess(DataEntity data) {
                                super.onSuccess(data);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
}
