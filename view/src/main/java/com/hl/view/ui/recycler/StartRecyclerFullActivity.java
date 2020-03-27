package com.hl.view.ui.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hl.base.BaseConstant;
import com.hl.base.adapter.StartFullRecyclerAdapter;
import com.hl.utils.StartPagerSnapHelper;
import com.hl.view.R;
import com.hl.view.ViewBaseActivity;

/**
 * 靠边对齐 全屏ViewPager 效果
 */
public class StartRecyclerFullActivity extends ViewBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_recycler);
        initToolbar(true);

        RecyclerView rv_base = findViewById(R.id.rv_base);

        StartPagerSnapHelper helper = new StartPagerSnapHelper(); // 靠边对齐 一次滑动一个item
        helper.attachToRecyclerView(rv_base);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 横屏滚动
        rv_base.setLayoutManager(linearLayoutManager);

        StartFullRecyclerAdapter adapter = new StartFullRecyclerAdapter(this);
        rv_base.setAdapter(adapter);

        adapter.setData(BaseConstant.getData(30));
        adapter.notifyDataSetChanged();
    }
}
