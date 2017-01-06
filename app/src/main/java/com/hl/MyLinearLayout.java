package com.hl;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * viewgroup
 */
public class MyLinearLayout extends LinearLayout {

    private static final String LOG_TAG = "MyLinearLayout";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(LOG_TAG, "viewgroup onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(LOG_TAG, "viewgroup dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(LOG_TAG, "viewgroup onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
