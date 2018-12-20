package com.hl.knowledge.touch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.hl.base.BaseActivity;
import com.hl.knowledge.R;
import com.hl.utils.L;

/**
 * 事件分发
 */
public class TouchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.know_activity_touch);
        initToolbar(true);

        Button btn_click =  findViewById(R.id.btn_click);
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("activity onClick");
            }
        });

        btn_click.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                L.e("activity onTouch");
                return false;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.e("activity onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        L.e("activity dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

}
