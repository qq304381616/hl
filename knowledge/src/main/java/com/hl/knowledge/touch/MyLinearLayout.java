package com.hl.knowledge.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hl.utils.L;

/**
 * ViewGroup
 */
public class MyLinearLayout extends LinearLayout {

    private TextView tv_log;

    public void setTv_log(TextView tv_log) {
        this.tv_log = tv_log;
    }

    private void addLog(String log) {
        tv_log.setText(tv_log.getText().toString() + "\n" + log);
    }

    @Override
    public boolean performClick() {
        addLog("ViewGroup performClick");
        L.e("ViewGroup performClick");
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        addLog("ViewGroup onTouchEvent");
        L.e("ViewGroup onTouchEvent");
        performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        addLog("ViewGroup dispatchTouchEvent");
        L.e("ViewGroup dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        addLog("ViewGroup onInterceptTouchEvent");
        L.e("ViewGroup onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
