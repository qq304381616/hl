package com.hl.knowledge.touch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.knowledge.R;
import com.hl.utils.L;

/**
 * 事件分发
 */
public class TouchActivity extends BaseActivity {

    private TextView tv_log;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.know_activity_touch);

        initToolbar(true);

        tv_log = findViewById(R.id.tv_log);
        MyButton btn_click = findViewById(R.id.btn_click);
        MyLinearLayout activity_main = findViewById(R.id.activity_main);
        btn_click.setTv_log(tv_log);
        activity_main.setTv_log(tv_log);

        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog("button onClick");
                L.e("button onClick");
            }
        });
        activity_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog("ViewGroup onClick");
                L.e("ViewGroup onClick");
            }
        });
        findViewById(R.id.tv_clear_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_log.setText("");
            }
        });
        addLog("日志（长按清空）：\n");
    }

    private void addLog(String log) {
        tv_log.setText(tv_log.getText().toString() + "\n" + log);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        addLog("activity onTouchEvent");
        L.e("activity onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        addLog("activity dispatchTouchEvent");
        L.e("activity dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
}
