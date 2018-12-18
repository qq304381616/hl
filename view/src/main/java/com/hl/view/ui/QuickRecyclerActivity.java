package com.hl.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hl.base.BaseConstant;
import com.hl.base.entity.BaseDataEntity;
import com.hl.base.utils.Utils;
import com.hl.utils.views.SideBar;
import com.hl.view.R;
import com.hl.view.ViewBaseActivity;
import com.hl.view.adapter.QuickAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Recycler 右侧字母 快速定位
 */
public class QuickRecyclerActivity extends ViewBaseActivity {

    private RecyclerView recycler;
    private QuickAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_quick_recycler);
        initToolbar(true);

        recycler = findViewById(R.id.recycler_quick);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuickAdapter(this);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        SideBar sideBar = findViewById(R.id.sidrbar);
        TextView dialog = findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // 设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recycler.scrollToPosition(position);
                }
            }
        });

        initData();
    }

    private void initData() {
        List<BaseDataEntity> data = BaseConstant.getData("张三", 5);
        data.addAll(BaseConstant.getData("李四", 5));
        data.addAll(BaseConstant.getData("王五", 5));
        data.addAll(BaseConstant.getData("阿凡达", 5));

        for (BaseDataEntity s : data) {
            s.setFirst(Utils.getLetter(s.getInfo()));
        }
        Collections.sort(data);

        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }
}
