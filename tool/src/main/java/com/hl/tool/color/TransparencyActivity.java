package com.hl.tool.color;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hl.base.BaseActivity;
import com.hl.tool.R;

public class TransparencyActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_activity_transparency);
        initToolbar(true);
    }
}
