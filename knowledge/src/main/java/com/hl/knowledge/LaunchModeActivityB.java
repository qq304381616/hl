package com.hl.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.utils.L;

/**
 * Created on 2016/11/17.
 */
public class LaunchModeActivityB extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e( "onCreate");

        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.CENTER);
        tv.setText("B");
        setContentView(tv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaunchModeActivityA.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        L.e( "onNewIntent");
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
