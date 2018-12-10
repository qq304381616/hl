package com.hl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hl.animation.AnimationMainActivity;
import com.hl.api.ApiMainActivity;
import com.hl.base.BaseActivity;
import com.hl.devices.DeviceMainActivity;
import com.hl.knowledge.KnowledgeMainActivity;
import com.hl.skin.SkinMainActivity;
import com.hl.systeminfo.SystemMainActivity;
import com.hl.utils.net.NetTestActivity;
import com.hl.view.ui.ViewMainActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_test).setOnClickListener(this);
        findViewById(R.id.tv_animation).setOnClickListener(this);
        findViewById(R.id.tv_design).setOnClickListener(this);
        findViewById(R.id.tv_knowledge).setOnClickListener(this);
        findViewById(R.id.tv_system).setOnClickListener(this);
        findViewById(R.id.tv_video).setOnClickListener(this);
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
                startActivity(new Intent(MainActivity.this, ViewMainActivity.class));
                break;
            case R.id.tv_knowledge:
                startActivity(new Intent(MainActivity.this, KnowledgeMainActivity.class));
                break;
            case R.id.tv_system:
                startActivity(new Intent(MainActivity.this, SystemMainActivity.class));
                break;
            case R.id.tv_video:
                startActivity(new Intent(MainActivity.this, VideoViewActivity.class));
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