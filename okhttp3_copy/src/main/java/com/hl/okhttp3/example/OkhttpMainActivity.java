package com.hl.okhttp3.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.hl.base.BaseActivity;
import com.hl.okhttp3.R;
import com.hl.okhttp3.core.OkHttpClient;
import com.hl.okhttp3.core.Request;
import com.hl.okhttp3.core.Response;
import com.hl.okhttp3.core.ResponseBody;
import com.hl.utils.L;

import java.io.IOException;

public class OkhttpMainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_activity_main);

        Button btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url("https://www.baidu.com").build();

                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ResponseBody body = response.body();

                        L.e(body);
                    }
                }).start();
            }
        });
    }
}
