package com.hl;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hl.animation.AnimationMainActivity;

import test1.com.design.DesignActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_animation).setOnClickListener(this);
        findViewById(R.id.tv_design).setOnClickListener(this);
        findViewById(R.id.tv_js).setOnClickListener(this);

        Log.e(LOG_TAG, "" + isTablet(this));
        Log.e(LOG_TAG, "" + getPackageName());

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
        }
    }

    /**
     * 是不平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
