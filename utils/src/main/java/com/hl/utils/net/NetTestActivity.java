package com.hl.utils.net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hl.utils.R;

/**
 * 网络请求测试
 */
public class NetTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NetTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nettest);
        findViewById(R.id.call).setOnClickListener(this);
        TextView tv_result = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call) {
//            NetUtils.weather("101010100", new HttpCallback() {
//                @Override
//                protected void onSuccess(Object dataEntity) {
//                    super.onSuccess(dataEntity);
//                    Gson gson = new Gson();
//                    DataEntity data = gson.fromJson(gson.toJson(dataEntity), new TypeToken<DataEntity>() {
//                    }.getType());
//                    LogUtils.e(TAG, data);
//                    LogUtils.e(TAG, data.getCity());
//                }
//            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtils.syncWeather("101010100", new HttpCallback() {
                        @Override
                        protected void onSuccess(Object data) {
                            super.onSuccess(data);
                        }
                    });
                }
            }).start();

        }
    }

}
