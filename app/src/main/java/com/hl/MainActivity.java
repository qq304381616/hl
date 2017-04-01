package com.hl;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hl.animation.AnimationMainActivity;
import com.hl.api.thread.ThreadActivity;
import com.hl.baidu.BaiduMainActivity;
import com.hl.design.DesignActivity;
import com.hl.skin.SkinMainActivity;
import com.hl.utils.PermissionUtils;
import com.hl.utils.net.NetTestActivity;
import com.hl.utils.net.OkHttpUtils;
import com.hl.widget.WidgetMainActivity;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_test).setOnClickListener(this);
        findViewById(R.id.tv_animation).setOnClickListener(this);
        findViewById(R.id.tv_design).setOnClickListener(this);
        findViewById(R.id.tv_js).setOnClickListener(this);
        findViewById(R.id.tv_okhttpget).setOnClickListener(this);
        findViewById(R.id.tv_touch).setOnClickListener(this);
        findViewById(R.id.tv_permission).setOnClickListener(this);
        findViewById(R.id.tv_system).setOnClickListener(this);
        findViewById(R.id.tv_coor).setOnClickListener(this);
        findViewById(R.id.tv_widget).setOnClickListener(this);
        findViewById(R.id.tv_map).setOnClickListener(this);
        findViewById(R.id.tv_myview).setOnClickListener(this);
        findViewById(R.id.tv_thread).setOnClickListener(this);
        findViewById(R.id.tv_video).setOnClickListener(this);
        findViewById(R.id.tv_message).setOnClickListener(this);
        findViewById(R.id.tv_imageplayer).setOnClickListener(this);
        findViewById(R.id.tv_baidu_location).setOnClickListener(this);
        findViewById(R.id.tv_net).setOnClickListener(this);
        findViewById(R.id.tv_skin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_test:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        getBitmapsFromVideo();
                    }
                }).start();

                break;
            case R.id.tv_animation:
                startActivity(new Intent(MainActivity.this, AnimationMainActivity.class));
                break;
            case R.id.tv_design:
                startActivity(new Intent(MainActivity.this, DesignActivity.class));
                break;
            case R.id.tv_js:
                startActivity(new Intent(MainActivity.this, WebActivity.class));
                break;
            case R.id.tv_okhttpget:
                OkHttpUtils.get();
                break;
            case R.id.tv_touch:
                startActivity(new Intent(MainActivity.this, TouchActivity.class));
                break;
            case R.id.tv_permission:
                PermissionUtils.check(this, Manifest.permission.READ_CONTACTS, PermissionUtils.READ_CONTACTS);
                break;
            case R.id.tv_system:
                startActivity(new Intent(MainActivity.this, SystemActivity.class));
                break;
            case R.id.tv_coor:
                break;
            case R.id.tv_widget:
                startActivity(new Intent(MainActivity.this, WidgetMainActivity.class));
                break;
            case R.id.tv_map:
                startActivity(new Intent(MainActivity.this, AndroidMapActivity.class));
                break;
            case R.id.tv_myview:
                startActivity(new Intent(MainActivity.this, MyViewActivity.class));
                break;
            case R.id.tv_thread:
                startActivity(new Intent(MainActivity.this, ThreadActivity.class));
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
        }
    }
}