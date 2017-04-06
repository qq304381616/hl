package com.hl.systeminfo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 显示所有已安装应用列表
 */
public class AppsInfoListActivity extends Activity {

    private static final String LOG_TAG = "AppsInfoListActivity";

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
                packs = getInstalledApps(getApplication());
                adapter.setData(packs);
                handler.sendEmptyMessage(0);
            }
        }).start();


    }

    /**
     * 获取应用列表
     */
    public static List<PackageInfo> getInstalledApps(Context c){
        List<PackageInfo> packs = c.getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packs) {
            Log.e(LOG_TAG, "name : " + p.applicationInfo.loadLabel(c.getPackageManager())
                    .toString());
            Log.e(LOG_TAG, "package : " + p.packageName);
            Log.e(LOG_TAG, "versionName : " + p.versionName);
            Log.e(LOG_TAG, "versionCode : " + p.versionCode);
            Log.e(LOG_TAG, "icon : " + p.applicationInfo.loadIcon(c.getPackageManager()));
        }
        return packs;
    }
}
