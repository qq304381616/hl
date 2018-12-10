package com.hl.knowledge.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.hl.utils.L;

/**
 * viewgroup
 */
public class MyLinearLayout extends LinearLayout {

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.e("viewgroup onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        L.e("viewgroup dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        L.e("viewgroup onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
