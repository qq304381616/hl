package com.hl.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hl.base.BaseActivity;
import com.hl.base.BaseConstant;
import com.hl.view.R;
import com.hl.view.adapter.ParentAdapter;

/**
 * Recycler 嵌套界面
 */
public class RecyclerDoubleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_recycler);
        initToolbar(true);

        RecyclerView rv_base = findViewById(R.id.rv_base);

        rv_base.setLayoutManager(new LinearLayoutManager(this));
        ParentAdapter adapter = new ParentAdapter(this);
        rv_base.setAdapter(adapter);
        rv_base.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setData(BaseConstant.getData("father"));
        adapter.notifyDataSetChanged();
    }
}
