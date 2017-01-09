package com.hl;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 显示所有已安装应用列表
 */
public class AppsInfoListActivity extends Activity {

    private List<PackageInfo> packs;
    private TextView tv_title;

    private AppInfoListRecyclerAdapter adapter;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_title.setText("应用列表（" + packs.size() + "）");
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_applistinfo_activity);

        tv_title = (TextView) findViewById(R.id.tv_title);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AppInfoListRecyclerAdapter(this);

        mRecyclerView.setAdapter(adapter);

        Toast.makeText(getApplicationContext(), "加载数据中", Toast.LENGTH_LONG).show();

       new Thread(new Runnable() {
            @Override
            public void run() {
                packs = Utils.getInstalledApps(getApplication());
                adapter.setData(packs);
                handler.sendEmptyMessage(0);
            }
        }).start();


    }


}
