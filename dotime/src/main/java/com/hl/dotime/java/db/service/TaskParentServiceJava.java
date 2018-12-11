package com.hl.dotime.java.db.service;

import android.content.Context;

import com.hl.dotime.TaskAdapter;
import com.hl.dotime.db.entity.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HL on 2018/5/19.
 */

public class TaskParentServiceJava {

    private Context mContext;

    private TaskAdapter adapter;
    public TaskParentServiceJava(Context context) {
        mContext = context;
    }

    private List<Task> qureyAll() {
        List<Task> list = new ArrayList<>();
        list.add(new Task());
        return list;
    }

    private void  test() {

        adapter = new TaskAdapter(mContext);
        adapter.setOnItemClick(null);

    }
}
