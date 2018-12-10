package com.hl.view.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hl.view.R;

/**
 * Created by HL on 2016/12/27.
 */

public class TestFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_activity_navigationview, container, false);
    }
}
