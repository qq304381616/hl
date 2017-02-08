package com.hl.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hl.widget.seekbar.ScaleView;

public class SeekbarActivity extends AppCompatActivity {

    private TextView tvNumber;
    private ScaleView scaleview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);

        tvNumber = (TextView)findViewById(R.id.tvNumber);
        scaleview = (ScaleView)findViewById(R.id.scaleview);
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
