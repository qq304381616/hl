package com.hl.design;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Android 5.0 material design
 */
public class DesignActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        findViewById(R.id.tv_SnackBar).setOnClickListener(this);
        findViewById(R.id.tv_navigationview).setOnClickListener(this);
        findViewById(R.id.tv_toolbar).setOnClickListener(this);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
//        tablayout.addTab(tablayout.newTab().setText("全部"));
//        tablayout.addTab(tablayout.newTab().setText("类别A"));
//        tablayout.addTab(tablayout.newTab().setText("类别B"));
        tablayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.holo_green_dark));
        tablayout.setSelectedTabIndicatorHeight(8);
        tablayout.setTabTextColors(Color.BLACK, getResources().getColor(android.R.color.holo_green_dark));

        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "专题1");
        adapter.addFragment(new Fragment1(), "专题2");
        adapter.addFragment(new Fragment1(), "专题3");
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_SnackBar) {
            Snackbar
                    .make(findViewById(R.id.root), "text", Snackbar.LENGTH_LONG)
                    .setAction("action", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplication(), "action click !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show(); // Don’t forget to show!
        } else if (id == R.id.tv_navigationview) {
            startActivity(new Intent(DesignActivity.this, NavigationViewActivity.class));
        } else if (id == R.id.tv_toolbar) {
            startActivity(new Intent(DesignActivity.this, ToolBarActivity.class));
        }
    }
}

class Adapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public Adapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
