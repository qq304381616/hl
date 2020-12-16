package com.hl.systeminfo.appinfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hl.base.BaseActivity;
import com.hl.base.dialog.DialogUtils;
import com.hl.base.utils.FirstComparator;
import com.hl.base.utils.Pinyin;
import com.hl.base.utils.Utils;
import com.hl.base.view.SearchView;
import com.hl.systeminfo.R;
import com.hl.utils.L;
import com.hl.utils.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 显示所有已安装应用列表
 */
public class AppsInfoListActivity extends BaseActivity {

    private List<AppEntity> packs;
    private AppInfoListRecyclerAdapter adapter;
    private SearchView search_view;
    private RecyclerView rv_app;
    private int mType = 1;

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

        initListener();
        updateView();
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
            if (mType == 2 && (p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {// 系统
                continue;
            } else if (mType == 3 && (p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) { // 用户
                continue;
            }
            AppEntity sm = new AppEntity();
            String name = p.applicationInfo.loadLabel(c.getPackageManager()).toString();
            sm.setName(name);
            sm.setFirst(Utils.getLetter(name));
            sm.setSimple(new Pinyin().setSimple(true).getPinYin(name));
            sm.setWhole(new Pinyin().getPinYin(name));
            sm.setPackageInfo(p);
            sms.add(sm);
        }
        Collections.sort(sms, new FirstComparator());
        return sms;
    }

    private void updateView() {
        final Dialog loading = DialogUtils.getLoading(this);
        loading.show();
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<List<AppEntity>>() {

            @Override
            public void subscribe(ObservableEmitter<List<AppEntity>> emitter) throws Exception {
                emitter.onNext(getInstalledApps(getApplication()));
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AppEntity>>() {
            @Override
            public void accept(List<AppEntity> s) throws Exception {
                loading.dismiss();
                setTitleText(getTitle() + " (" + s.size() + ")");
                packs = s;
                adapter.setData(packs);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_do_time) {
            L.e("menu....");
            showSettingDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置");
        View view = LayoutInflater.from(this).inflate(R.layout.layout_app_setting, null);
        final RadioGroup tg_type = view.findViewById(R.id.rg_type);
        if (mType == 2) {
            tg_type.check(R.id.rb_system);
        } else if (mType == 3) {
            tg_type.check(R.id.rb_user);
        } else {
            tg_type.check(R.id.rb_all);
        }
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int i = tg_type.getCheckedRadioButtonId();
                if (i == R.id.rb_all) {
                    mType = 1;
                } else if (i == R.id.rb_system) {
                    mType = 2;
                } else if (i == R.id.rb_user) {
                    mType = 3;
                }
                updateView();
            }
        });
        builder.show();
    }
}
