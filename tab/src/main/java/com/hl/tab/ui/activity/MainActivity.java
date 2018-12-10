package com.hl.tab.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.tab.R;
import com.hl.tab.ui.adapter.MainPagerAdapter;
import com.hl.tab.view.PagerSlidingTab;

public class MainActivity extends BaseActivity {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawLayout;
    private PagerSlidingTab pagerSlidingTab;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_main);
        initView();
        initActionBar();
    }

    /**
     * 初始化View
     */
    private void initView() {
        drawLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerSlidingTab = (PagerSlidingTab) findViewById(R.id.pagerSlidingTab);
        viewPager.setAdapter(new MainPagerAdapter(this, getSupportFragmentManager()));
        pagerSlidingTab.setViewPager(viewPager);/*绑定pagerSlidingTab和ViewPager*/
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);/*设置actionbar上面的icon*/
        actionBar.setTitle(getString(R.string.app_name));/*设置actionbar上面的name*/
        actionBar.setDisplayHomeAsUpEnabled(true);/*显示home按钮*/
        actionBar.setDisplayShowHomeEnabled(true);/*设置home按钮可以被点击*/
        drawerToggle = new ActionBarDrawerToggle(this, drawLayout, 0, 0);/*关联ActionBar和DrawLayout*/
        drawLayout.setDrawerListener(new DrawerLayout.DrawerListener() {/*给可点击图标增加动画*/
            @Override
            public void onDrawerStateChanged(int newState) {
                drawerToggle.onDrawerStateChanged(newState);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                drawerToggle.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerToggle.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerToggle.onDrawerClosed(drawerView);
            }
        });
        drawerToggle.syncState();/*将ActionBar的状态和drawLayout同步起来*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:/*actionbar上面可点击图标的点击事件*/
                drawerToggle.onOptionsItemSelected(item);
                break;
        }
        return true;
    }
}