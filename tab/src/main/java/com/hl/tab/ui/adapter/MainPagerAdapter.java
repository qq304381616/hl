package com.hl.tab.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hl.tab.R;
import com.hl.tab.ui.fragment.FragmentFactory;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String[] tabArr;

    public MainPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        tabArr =  context.getResources().getStringArray(R.array.tab_names);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentFactory.create(position);
    }

    @Override
    public int getCount() {
        return tabArr.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabArr[position];
    }
}