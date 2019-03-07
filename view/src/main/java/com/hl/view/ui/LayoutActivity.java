package com.hl.view.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.view.R;
import com.hl.view.adapter.ViewMainAdapter;

/**
 * 布局控件
 */
public class LayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_layout);
        initToolbar(true);

        findViewById(R.id.tv_constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LayoutActivity.this, ConstraintLayoutActivity.class));
            }
        });

        findViewById(R.id.tv_nestedScrollView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LayoutActivity.this, NestedScrollViewActivity.class));
            }
        });

        findViewById(R.id.tv_coordinatorlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LayoutActivity.this, CoordinatorlayoutActivity.class));
            }
        });

        findViewById(R.id.tv_navigationview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LayoutActivity.this, NavigationViewActivity.class));
            }
        });

        TabLayout tablayout = findViewById(R.id.tablayout);
//        tablayout.addTab(tablayout.newTab().setText("全部"));
//        tablayout.addTab(tablayout.newTab().setText("类别A"));
//        tablayout.addTab(tablayout.newTab().setText("类别B"));
        tablayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.holo_green_dark));
        tablayout.setSelectedTabIndicatorHeight(8);
        tablayout.setTabTextColors(Color.BLACK, getResources().getColor(android.R.color.holo_green_dark));

        ViewPager viewpager = findViewById(R.id.viewpager);
        ViewMainAdapter adapter = new ViewMainAdapter(getSupportFragmentManager());
        adapter.addFragment(new TestFragment(), "专题1");
        adapter.addFragment(new TestFragment(), "专题2");
        adapter.addFragment(new TestFragment(), "专题3");
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
    }
}
