package com.hl.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.base.view.seekbar.ScaleView;
import com.hl.view.R;

public class SeekbarActivity extends BaseActivity {

    private TextView tvNumber;
    private ScaleView scaleview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_seekbar);

        tvNumber = (TextView) findViewById(R.id.tvNumber);
        scaleview = (ScaleView) findViewById(R.id.scaleview);
        scaleview.setMaxNumber(1000);
        scaleview.setMinNumber(-1000);
        scaleview.setScaleNumber(10);
        scaleview.setAllBlockNum(90);
        scaleview.setTextSize(30);
        scaleview.setCenterNum(100);
        scaleview.setNumberListener(new ScaleView.NumberListener() {
            @Override
            public void onChanged(int mCurrentNum) {
                tvNumber.setText(mCurrentNum + "");
            }
        });
    }
}
