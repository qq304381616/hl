package com.hl;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hl.animation.AnimationMainActivity;
import com.hl.utils.net.OkHttpUtils;
import com.hl.utils.PermissionUtils;

import com.hl.design.DesignActivity;
import com.hl.widget.WidgetMainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_animation).setOnClickListener(this);
        findViewById(R.id.tv_design).setOnClickListener(this);
        findViewById(R.id.tv_js).setOnClickListener(this);
        findViewById(R.id.tv_okhttpget).setOnClickListener(this);
        findViewById(R.id.tv_touch).setOnClickListener(this);
        findViewById(R.id.tv_permission).setOnClickListener(this);
        findViewById(R.id.tv_recyclerviewr).setOnClickListener(this);
        findViewById(R.id.tv_coor).setOnClickListener(this);
        findViewById(R.id.tv_widget).setOnClickListener(this);
        findViewById(R.id.tv_map).setOnClickListener(this);
        findViewById(R.id.tv_video).setOnClickListener(this);
        findViewById(R.id.tv_message).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
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
                PermissionUtils.check(this);
                break;
            case R.id.tv_recyclerviewr:
                startActivity(new Intent(MainActivity.this, RecycleViewActivity.class));
                break;
            case R.id.tv_coor:
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                Log.e(LOG_TAG, "onRequestPermissionsResult");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
