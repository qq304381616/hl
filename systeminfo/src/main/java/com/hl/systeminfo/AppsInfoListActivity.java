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

import com.hl.systeminfo.appinfo.SortModel;
import com.hl.utils.PinyinUtils;
import com.hl.utils.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 显示所有已安装应用列表
 */
public class AppsInfoListActivity extends Activity {

    private static final String LOG_TAG = "AppsInfoListActivity";

    private List<SortModel> packs;
    private TextView tv_title;

    private AppInfoListRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private TextView dialog;

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

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        // 设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mRecyclerView.scrollToPosition(position);
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
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
    public List<SortModel> getInstalledApps(Context c) {

        List<SortModel> sms = new ArrayList<>();
        List<PackageInfo> packs = c.getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packs) {
            SortModel sm = new SortModel();
            String name = p.applicationInfo.loadLabel(c.getPackageManager()).toString();
            String nameSort = PinyinUtils.getPinyinFirstLetter(name).toLowerCase();
            Log.e(LOG_TAG, "name : " + name);
            Log.e(LOG_TAG, "package : " + p.packageName);
            Log.e(LOG_TAG, "versionName : " + p.versionName);
            Log.e(LOG_TAG, "versionCode : " + p.versionCode);
            Log.e(LOG_TAG, "icon : " + p.applicationInfo.loadIcon(c.getPackageManager()));
            Log.e(LOG_TAG, "nameSort : " + nameSort);
            sm.setSortLetters(nameSort);
            sm.setPackageInfo(p);
            sms.add(sm);
        }
        Collections.sort(sms, new PinyinComparator());
        return sms;
    }

    class PinyinComparator implements Comparator<SortModel> {

        public int compare(SortModel o1, SortModel o2) {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
