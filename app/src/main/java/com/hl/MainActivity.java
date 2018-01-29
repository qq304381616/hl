package com.hl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hl.animation.AnimationMainActivity;
import com.hl.api.ApiMainActivity;
import com.hl.baidu.BaiduMainActivity;
import com.hl.design.DesignActivity;
import com.hl.devices.DeviceMainActivity;
import com.hl.knowledge.KnowledgeMainActivity;
import com.hl.skin.SkinMainActivity;
import com.hl.systeminfo.SystemMainActivity;
import com.hl.utils.net.NetTestActivity;
import com.hl.widget.WidgetMainActivity;
import com.hl.widget.h5.H5MainActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_test).setOnClickListener(this);
        findViewById(R.id.tv_animation).setOnClickListener(this);
        findViewById(R.id.tv_design).setOnClickListener(this);
        findViewById(R.id.tv_js).setOnClickListener(this);
        findViewById(R.id.tv_knowledge).setOnClickListener(this);
        findViewById(R.id.tv_permission).setOnClickListener(this);
        findViewById(R.id.tv_system).setOnClickListener(this);
        findViewById(R.id.tv_widget).setOnClickListener(this);
        findViewById(R.id.tv_map).setOnClickListener(this);
        findViewById(R.id.tv_video).setOnClickListener(this);
        findViewById(R.id.tv_message).setOnClickListener(this);
        findViewById(R.id.tv_imageplayer).setOnClickListener(this);
        findViewById(R.id.tv_baidu_location).setOnClickListener(this);
        findViewById(R.id.tv_net).setOnClickListener(this);
        findViewById(R.id.tv_skin).setOnClickListener(this);
        findViewById(R.id.api).setOnClickListener(this);
        findViewById(R.id.device).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_test:
                break;
            case R.id.tv_animation:
                startActivity(new Intent(MainActivity.this, AnimationMainActivity.class));
                break;
            case R.id.tv_design:
                startActivity(new Intent(MainActivity.this, DesignActivity.class));
                break;
            case R.id.tv_js:
                startActivity(new Intent(MainActivity.this, H5MainActivity.class));
                break;
            case R.id.tv_knowledge:
                startActivity(new Intent(MainActivity.this, KnowledgeMainActivity.class));
                break;
            case R.id.tv_permission:
                startActivity(new Intent(MainActivity.this, PermissionActivity.class));
                break;
            case R.id.tv_system:
                startActivity(new Intent(MainActivity.this, SystemMainActivity.class));
                break;
            case R.id.tv_widget:
                startActivity(new Intent(MainActivity.this, WidgetMainActivity.class));
                break;
            case R.id.tv_map:
                startActivity(new Intent(MainActivity.this, AndroidMapActivity.class));
                break;
            case R.id.tv_video:
                startActivity(new Intent(MainActivity.this, VideoViewActivity.class));
                break;
            case R.id.tv_message:
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                break;
            case R.id.tv_imageplayer:
                startActivity(new Intent(MainActivity.this, ImagePlayerActivity.class));
                break;
            case R.id.tv_baidu_location:
                com.hl.baidu.service.Utils.init(MainActivity.this); // TODO  初始化，原Demo在 application中执行。
                startActivity(new Intent(MainActivity.this, BaiduMainActivity.class));
                break;
            case R.id.tv_net:
                startActivity(new Intent(MainActivity.this, NetTestActivity.class));
                break;
            case R.id.tv_skin:
                startActivity(new Intent(MainActivity.this, SkinMainActivity.class));
                break;
            case R.id.api:
                startActivity(new Intent(MainActivity.this, ApiMainActivity.class));
                break;
            case R.id.device:
                startActivity(new Intent(MainActivity.this, DeviceMainActivity.class));
                break;
        }
    }
}