package com.hl.view.ui.toolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.hl.base.BaseActivity;
import com.hl.view.R;
import com.hl.view.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Toolbar的扩展与收缩, 上下拉动 ，显示与隐藏ToolBar
 */
public class ToolBarAppBarLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_design_activity_toolbar_appbarlayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("title");
        setSupportActionBar(toolbar);

        List<String> list = new ArrayList<String>();
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        list.add("111");
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerAdapter(this, list));
    }
}
