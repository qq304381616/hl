package com.hl.animation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hl.base.BaseActivity;

/**
 * Created by yuandl on 2016-11-08.
 */

public class SecondActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_ac_second);
    }
    public void onClick(View v){
        finish();
    }
}
