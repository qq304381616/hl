package com.hl.widget.head;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hl.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 带头而已的recycler_view
 */
public class HeadRecyclerActivity extends AppCompatActivity {

    private HeadAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_recycler);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mAdapter = new HeadAdapter(this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //添加动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("aa");
        list.add("bb");
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
    }
}
