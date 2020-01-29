package com.hl.view.ui.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hl.base.BaseConstant;
import com.hl.base.adapter.DeleteRecyclerAdapter;
import com.hl.utils.StartLinearSnapHelper;
import com.hl.view.R;
import com.hl.view.ViewBaseActivity;

/**
 * 靠边对齐
 * 使用 SnapHelper 接口实现。默认居中停靠
 * 两个实现类：
 * LinearSnapHelper： 快速滚动
 * PagerSnapHelper： 一次滚动一个item
 *
 * StartLinearSnapHelper 继承LinearSnapHelper。实现边停靠
 */
public class StartRecyclerActivity extends ViewBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_recycler);
        initToolbar(true);

        RecyclerView rv_base = findViewById(R.id.rv_base);

        StartLinearSnapHelper helper = new StartLinearSnapHelper();
        helper.attachToRecyclerView(rv_base);

        rv_base.setLayoutManager(new LinearLayoutManager(this));
        DeleteRecyclerAdapter adapter = new DeleteRecyclerAdapter(this);
        rv_base.setAdapter(adapter);
        rv_base.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setData(BaseConstant.getData(30));
        adapter.notifyDataSetChanged();
    }
}
