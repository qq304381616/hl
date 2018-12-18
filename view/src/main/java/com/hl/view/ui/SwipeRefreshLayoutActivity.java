package com.hl.view.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import com.hl.view.R;
import com.hl.view.ViewBaseActivity;

/**
 * google 下拉刷新组件
 */
public class SwipeRefreshLayoutActivity extends ViewBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_swiperefreshlayout);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        final TextView widget_tv = findViewById(R.id.widget_tv);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                widget_tv.setText("正在刷新");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        widget_tv.setText("刷新完成");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });
    }
}
