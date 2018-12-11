package com.hl.dotime.java;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hl.dotime.R;
import com.hl.dotime.db.service.TaskService;

/**
 * Created by HL on 2018/5/16.
 */

public class TaskFragmentJava extends Fragment {

    private static final String EXTRA_CONTENT = "content";

    public static TaskFragmentJava newInstance(String content) {

        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_CONTENT, content);
        TaskFragmentJava tabContentFragment = new TaskFragmentJava();
        tabContentFragment.setArguments(arguments);
        return tabContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_task, null);

        TaskService taskParentService = new TaskService(getActivity());
        return contentView;
    }
}
