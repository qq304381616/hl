package com.hl.utils.net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hl.utils.R;

public class NetTestActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nettest);
        findViewById(R.id.call).setOnClickListener(this);
        tv_result = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call) {
//            test1();

            NetUtils.test2();
//            NetUtils.test3();
//            NetUtils.test4();

        }
    }

    public void test1() {
        RetrofitUtils.init();
        RetrofitUtils.test();
    }

}
