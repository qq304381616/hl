package com.hl.knowledge.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.hl.utils.L;

/**
 * 自定义view
 */
public class MyButton extends Button {

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        L.e("view dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.e("view onTouchEvent");
        return super.onTouchEvent(event);
    }

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
