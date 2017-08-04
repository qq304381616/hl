package com.hl.widget.h5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hl.widget.R;

/**
 * Created on 2017/8/3.
 * h5 webview js交互相关主页
 */
public class H5MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_activity_main_h5);
        findViewById(R.id.tv_js1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(H5MainActivity.this, WebActivity.class));
            }
        });
        findViewById(R.id.tv_js2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(H5MainActivity.this, JSWebViewActivity.class));
            }
        });
    }
}
