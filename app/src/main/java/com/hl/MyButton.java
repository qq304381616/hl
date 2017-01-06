package com.hl;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 自定义view
 */
public class MyButton extends Button {

    private static final String LOG_TAG = "MyButton";

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(LOG_TAG, "view dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(LOG_TAG, "view onTouchEvent");
        return super.onTouchEvent(event);
    }

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
