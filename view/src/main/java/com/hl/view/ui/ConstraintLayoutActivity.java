package com.hl.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hl.base.BaseActivity;
import com.hl.view.R;

public class ConstraintLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_constraintlayout);
        initToolbar(true);

        String text = "layout_constraintLeft_toLeftOf 相对于控件上下左右的位置，值id/parent \n" +
                "0dp 占用剩余全部空间\n" +
                "layout_constraintDimensionRatio 宽高比例" +
                "Guideline : layout_constraintGuide_begin指定具体偏移值，layout_constraintGuide_percent可指定偏移百分比。";

    }
}
