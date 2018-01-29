package com.hl.systeminfo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

/**
 * Created by HL on 2017/9/13.
 */
public class FragmentActivity extends android.support.v4.app.FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyFragment myFragment = new MyFragment();

        FrameLayout fl = new FrameLayout(this);
        fl.setId(R.id.id_ll_ok);
        setContentView(fl);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.id_ll_ok, myFragment);
        transaction.commit();
    }
}
