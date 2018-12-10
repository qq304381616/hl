package com.hl.systeminfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.utils.L;

/**
 * Created on 2016/11/17.
 */
public class LaunchModeActivityA extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("onCreate");

        StringBuffer sb = new StringBuffer();
        sb.append("A\n\n");
        sb.append("1, 测试 Manifest 和代码setFlags 同时设置Activity启动模式时。singleTask 比singleTop 优化级高更高。和以哪种方式设置无关。\n");
        sb.append("FLAG_ACTIVITY_CLEAR_TOP == singleTask\n\n");
        sb.append("2, 一个ActivityA 跳转到ActivityB再跳转到ActivityA .生命周期如下：\n");
        sb.append("ACreate AStart AResume APause BCreate BStart BResume AStop BPause ARestart AStart AResume BStop BDestory\n\n");

        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.CENTER);
        tv.setText(sb.toString());

        setContentView(tv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaunchModeActivityB.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        L.e("onNewIntent");
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.e("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.e("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.e("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.e("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("onDestroy");
    }
}
