package com.hl.systeminfo.appinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.hl.base.BaseActivity;
import com.hl.base.utils.Pinyin;
import com.hl.base.utils.Utils;
import com.hl.base.view.SearchView;
import com.hl.systeminfo.R;
import com.hl.utils.L;
import com.hl.utils.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 显示所有已安装应用列表
 */
public class AppsInfoListActivity extends BaseActivity {

    private List<AppEntity> packs;
    private AppInfoListRecyclerAdapter adapter;
    private SearchView search_view;
    private RecyclerView rv_app;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_app);
        initToolbar(true);

        SideBar sideBar = findViewById(R.id.sidrbar);
        TextView dialog = findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        rv_app = findViewById(R.id.rv_app);
        search_view = findViewById(R.id.search_view);

        rv_app.setLayoutManager(new LinearLayoutManager(this));
        rv_app.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new AppInfoListRecyclerAdapter(this);
        rv_app.setAdapter(adapter);
        sideBar.initListener(adapter, rv_app);

        Toast.makeText(getApplicationContext(), "加载数据中", Toast.LENGTH_LONG).show();

        initListener();

        new Thread(new Runnable() {
            @Override
            public void run() {
                packs = getInstalledApps(getApplication());
                adapter.setData(packs);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initListener() {
        search_view.setCallback(new SearchView.Callback() {
            @Override
            public void onQueryTextSubmit(String s) {

            }

            @Override
            public void onQueryTextChange(String s) {
                if (s.length() > 0) {
                    adapter.setData(search(s));
                } else {
                    adapter.setData(packs);
                }
                adapter.setSearchText(s);
                adapter.notifyDataSetChanged();
                rv_app.scrollToPosition(0);
            }
        });
    }

    /**
     * 模糊查询
     */
    private List<AppEntity> search(String str) {
        List<AppEntity> filterList = new ArrayList<>();// 过滤后的list
        List<String> list = new ArrayList<>();
        for (AppEntity app : packs) {
            if (app.getName().contains(str) || app.getWhole().contains(str) || app.getSimple().contains(str) || app.getPackageInfo().packageName.contains(str)) {
                if (!list.contains(app.getName())) {
                    filterList.add(app);
                    list.add(app.getName());
                }
            }
        }
        return filterList;
    }

    /**
     * 获取应用列表
     */
    public List<AppEntity> getInstalledApps(Context c) {
        List<AppEntity> sms = new ArrayList<>();
        List<PackageInfo> packs = c.getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packs) {
            AppEntity sm = new AppEntity();
            String name = p.applicationInfo.loadLabel(c.getPackageManager()).toString();
            String nameSort = Utils.getLetter(name);
            L.e("name : " + name);
            L.e("package : " + p.packageName);
            L.e("versionName : " + p.versionName);
            L.e("versionCode : " + p.versionCode);
            L.e("icon : " + p.applicationInfo.loadIcon(c.getPackageManager()));
            L.e("nameSort : " + nameSort);
            sm.setName(name);
            sm.setFirst(Utils.getLetter(name));
            sm.setSimple(new Pinyin().setSimple(true).getPinYin(name));
            sm.setWhole(new Pinyin().getPinYin(name));
            sm.setPackageInfo(p);
            sms.add(sm);
        }
        Collections.sort(sms);
        return sms;
    }
}
