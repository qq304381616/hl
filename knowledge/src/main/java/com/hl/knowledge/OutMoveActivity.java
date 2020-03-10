package com.hl.knowledge;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.utils.ToastUtils;

/**
 * 监听屏幕外部滑入事件
 */
public class OutMoveActivity extends BaseActivity {

    private GestureHandler gestureHandler;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return gestureHandler.doEventFling(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_test);
        initToolbar(true);

        TextView tv_message = findViewById(R.id.tv_message);
        tv_message.setText("左右屏幕外向内滑动 触发事件");

        gestureHandler = new GestureHandler();
    }

    class GestureHandler {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //屏幕宽高
        int sWidth = wm.getDefaultDisplay().getWidth();
        int sHeight = wm.getDefaultDisplay().getHeight();
        //按下的点
        PointF down;
        //Y轴滑动的区间
        float minY, maxY;
        //按下时的时间
        long downTime;
        //边缘判定距离，
        double margin = sWidth * 0.3;
        //Y轴最大区间范围，即Y轴滑动超出这个范围不触发事件
        double height = sHeight * 0.1;
        //X轴最短滑动距离 X轴滑动范围低于此值不触发事件
        double width = sWidth * 0.05;
        //是否处于此次滑动事件
        boolean work = false;

        private boolean doEventFling(MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    //记录下按下的点
                    downTime = System.currentTimeMillis();
                    down = new PointF(event.getX(), event.getY());
                    minY = maxY = down.y;
                    //判定是否处于边缘侧滑
                    if (down.x < margin || (sWidth - down.x) < margin) work = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    //记录滑动Y轴区间
                    if (work)
                        if (event.getY() > down.y) {
                            maxY = event.getY();
                        } else {
                            minY = event.getY();
                        }
                    break;
                case MotionEvent.ACTION_UP:
                    if (work) {
                        handle(new PointF(event.getX(), event.getY()));
                        work = false;
                        return true;  // 如果不想消费此事件，需要改成 return false
                    }
                    work = false;
            }
            // 如果当前处于边缘滑动判定过程中，则消费掉此事件不往下传递。
            return work;  // 如果不想消费此事件，需要改成 return false
        }

        private boolean handle(PointF up) {
            long upTime = System.currentTimeMillis();
            float tWidth = Math.abs(down.x - up.x);
            if (maxY - minY < height && tWidth > width && (upTime - downTime) / tWidth < 2.5) {
                //起点在左边
                if (down.x < margin) {
                    if (down.x > up.x) { // 左侧左滑
                        leftToLeft();
                    } else {  // 左侧右滑
                        leftToRight();
                    }
                    return true;
                }
                //起点在右边
                if ((sWidth - down.x) < margin) {
                    if (down.x <= up.x) { // 右侧右滑
                        rightToRight();
                    } else {
                        rightToLeft(); // 右侧左滑
                    }
                    return true;
                }
            }
            return false;
        }

        private void leftToLeft() {
            ToastUtils.showShortToast(getApplicationContext(), "left to left event");
        }

        private void leftToRight() {
            ToastUtils.showShortToast(getApplicationContext(), "left to right event");
        }

        private void rightToRight() {
            ToastUtils.showShortToast(getApplicationContext(), "right to right event");
        }

        private void rightToLeft() {
            ToastUtils.showShortToast(getApplicationContext(), "right to left event");
        }
    }
}
