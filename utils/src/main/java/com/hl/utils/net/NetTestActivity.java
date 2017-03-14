package com.hl.utils.net;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hl.utils.LogUtils;
import com.hl.utils.R;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.functions.Action0;

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

//            NetUtils.test2();
            NetUtils.test3();
//            NetUtils.test4();

        }
    }

    public void test1() {
        RetrofitUtils.init();
        RetrofitUtils.test();
    }

    public void test6() {
        NetUtils.test6("").doOnSubscribe(new Action0() {
            @Override
            public void call() {
                LogUtils.e("TAG", "doOnSubscribe 3");
            }
        }).subscribe(new Observer<RetrofitEntity>() {
            @Override
            public void onCompleted() {
                LogUtils.e("TAG", "onCompleted 3");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtils.e("TAG", "onError 3 " + e.getMessage());
            }

            @Override
            public void onNext(RetrofitEntity verification) {
                verification.getMsg();
                LogUtils.e("TAG", "onNext 3 : " + verification.getMsg());
            }
        });
    }

}
