package com.hl.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.view.R;

/**
 * 基本控件
 */
public class WidgetActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_widget);

        findViewById(R.id.textureview).setOnClickListener(this);
        findViewById(R.id.tv_swiperefreshlayout).setOnClickListener(this);
        findViewById(R.id.tv_refresh).setOnClickListener(this);
        findViewById(R.id.tv_recycler_head).setOnClickListener(this);
        findViewById(R.id.tv_recyclerviewdouble).setOnClickListener(this);
        findViewById(R.id.tv_quick_recyclerview).setOnClickListener(this);
        findViewById(R.id.tv_seekbar).setOnClickListener(this);
        findViewById(R.id.tv_banner).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.textureview) {
            startActivity(new Intent(WidgetActivity.this, TextureViewActivity.class));
        } else if (id == R.id.tv_swiperefreshlayout) {
            startActivity(new Intent(WidgetActivity.this, SwipeRefreshLayoutActivity.class));
        } else if (id == R.id.tv_refresh) {
            startActivity(new Intent(WidgetActivity.this, SwipeRefreshRecyclerActivity.class));
        } else if (id == R.id.tv_recycler_head) {
            startActivity(new Intent(WidgetActivity.this, HeadRecyclerActivity.class));
        } else if (id == R.id.tv_seekbar) {
            startActivity(new Intent(WidgetActivity.this, SeekbarActivity.class));
        } else if (id == R.id.tv_recyclerviewdouble) {
            startActivity(new Intent(WidgetActivity.this, RecyclerDoubleActivity.class));
        } else if (id == R.id.tv_quick_recyclerview) {
            startActivity(new Intent(WidgetActivity.this, QuickRecyclerActivity.class));
        } else if (id == R.id.tv_banner) {
            startActivity(new Intent(WidgetActivity.this, BannerActivity.class));
        }
    }
}
