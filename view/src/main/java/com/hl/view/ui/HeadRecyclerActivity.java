package com.hl.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hl.base.BaseActivity;
import com.hl.base.BaseConstant;
import com.hl.base.adapter.DeleteRecyclerAdapter;
import com.hl.view.R;
import com.hl.view.adapter.HeadAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 带头而已的RecyclerView
 */
public class HeadRecyclerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_recycler);
        initToolbar(true);

        RecyclerView rv_base =  findViewById(R.id.rv_base);

        HeadAdapter adapter = new HeadAdapter(this);
        rv_base.setLayoutManager(new LinearLayoutManager(this));
        rv_base.setAdapter(adapter);
        rv_base.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setData(BaseConstant.getData());
        adapter.notifyDataSetChanged();
    }
}
