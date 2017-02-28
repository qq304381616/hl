package com.hl.widget.recyclerdouble;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hl.widget.R;

import java.util.ArrayList;

/**
 * Recycler 嵌套界面
 */
public class RecyclerDoubleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerdouble);
        RecyclerView recyclerParent = (RecyclerView) findViewById(R.id.recycler_parent);

        recyclerParent.setLayoutManager(new LinearLayoutManager(this));
        ParentAdapter adapter = new ParentAdapter(this);
        recyclerParent.setAdapter(adapter);
        recyclerParent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        ArrayList<String> datas = new ArrayList<String>();
        datas.add("parent1");
        datas.add("parent2");
        datas.add("parent3");
        datas.add("parent4");
        datas.add("parent5");
        adapter.setData(datas);
        adapter.notifyDataSetChanged();
    }
}
