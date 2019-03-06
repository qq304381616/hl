package com.hl.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.view.R;

public class NestedScrollViewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_nested_scroll_view);
        initToolbar(true);

        String str = "android.support.v4.widget.NestedScrollView \n" +
                "相比 ScrollerView \n" +
                "支持嵌套滑动";

        TextView text = findViewById(R.id.text);

        text.setText(str);
    }
}
