package com.hl.knowledge.touch;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hl.utils.L;

/**
 * 自定义view
 */
public class MyButton extends AppCompatButton {

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        L.e("view dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        L.e("view performClick");
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
