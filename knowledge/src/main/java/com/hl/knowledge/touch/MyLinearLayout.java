package com.hl.knowledge.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.hl.utils.L;

/**
 * viewgroup
 */
public class MyLinearLayout extends LinearLayout {

    @Override
    public boolean performClick() {
        L.e("ViewGroup performClick");
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.e("ViewGroup onTouchEvent");
        performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        L.e("ViewGroup dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
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
