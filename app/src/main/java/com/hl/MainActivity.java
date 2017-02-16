package com.hl;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hl.animation.AnimationMainActivity;
import com.hl.utils.net.OkHttpUtils;
import com.hl.utils.PermissionUtils;

import com.hl.design.DesignActivity;
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
    }

    /**
     * 视频转成图片，间隔1秒
     */
    public void getBitmapsFromVideo() {
        String dataPath = Environment.getExternalStorageDirectory() + "/9/c.mp4";
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(dataPath);
        // 取得视频的长度(单位为毫秒)
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        // 取得视频的长度(单位为秒)
        int seconds = Integer.valueOf(time) / 1000;
        // 得到每一秒时刻的bitmap比如第一秒,第二秒
        for (int i = 1; i <= seconds; i++) {
            Bitmap bitmap = retriever.getFrameAtTime(i * 1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            String path = Environment.getExternalStorageDirectory() +  "/8/" + i + ".jpg";
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_test:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getBitmapsFromVideo();
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
                PermissionUtils.check(this);
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