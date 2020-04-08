package com.hl.knowledge.touch;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.hl.utils.L;

/**
 * 自定义view
 */
public class MyButton extends AppCompatButton {

    private TextView tv_log;

    public void setTv_log(TextView tv_log) {
        this.tv_log = tv_log;
    }

    private void addLog(String log) {
        tv_log.setText(tv_log.getText().toString() + "\n" + log);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        addLog("view dispatchTouchEvent");
        L.e("view dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        addLog("view performClick");
        L.e("view performClick");
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        addLog("view onTouchEvent");
        L.e("view onTouchEvent");
        performClick();
        return super.onTouchEvent(event);
    }

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
