package com.hl.knowledge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.hl.base.BaseActivity;

/**
 * 在两个ViewGroup里移动View
 */
public class MoveViewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.know_activity_move_view);

        final RelativeLayout view_1 = findViewById(R.id.view_1);
        final RelativeLayout view_2 = findViewById(R.id.view_2);
        final View tv_1 = findViewById(R.id.tv_1);

        findViewById(R.id.tv_left_to_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_1.getChildAt(0) != null) {
                    view_1.removeView(tv_1);
                    view_2.addView(tv_1);
                }
            }
        });

        findViewById(R.id.tv_right_to_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_2.getChildAt(0) != null) {
                    view_2.removeView(tv_1);
                    view_1.addView(tv_1);
                }
            }
        });
    }
}
