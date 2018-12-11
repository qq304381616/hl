package com.hl.view.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.hl.base.BaseActivity;
import com.hl.view.R;
import com.hl.view.adapter.ViewMainAdapter;
import com.hl.view.ui.dialog.DialogMainActivity;

public class ViewMainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_activity_view_main);
        findViewById(R.id.tv_easy).setOnClickListener(this);
        findViewById(R.id.tv_dialog).setOnClickListener(this);
        findViewById(R.id.tv_SnackBar).setOnClickListener(this);
        findViewById(R.id.tv_navigationview).setOnClickListener(this);
        findViewById(R.id.tv_coordinatorlayout).setOnClickListener(this);
        findViewById(R.id.tv_toolbar).setOnClickListener(this);
        findViewById(R.id.tv_toolbar2).setOnClickListener(this);
        findViewById(R.id.tv_toolbar3).setOnClickListener(this);
        findViewById(R.id.tv_page_sliding_tab).setOnClickListener(this);

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
        } else if (id == R.id.tv_easy) {
            startActivity(new Intent(ViewMainActivity.this, WidgetActivity.class));
        } else if (id == R.id.tv_dialog) {
            startActivity(new Intent(ViewMainActivity.this, DialogMainActivity.class));
        } else if (id == R.id.tv_navigationview) {
            startActivity(new Intent(ViewMainActivity.this, NavigationViewActivity.class));
        } else if (id == R.id.tv_page_sliding_tab) {
            Intent intent = new Intent(ViewMainActivity.this, com.hl.tab.ui.activity.MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.tv_coordinatorlayout) {
            startActivity(new Intent(ViewMainActivity.this, CoordinatorlayoutActivity.class));
        } else if (id == R.id.tv_toolbar) {
            startActivity(new Intent(ViewMainActivity.this, ToolBarActivity.class));
        } else if (id == R.id.tv_toolbar2) {
            startActivity(new Intent(ViewMainActivity.this, ToolBarAppBarLayoutActivity.class));
        } else if (id == R.id.tv_toolbar3) {
            startActivity(new Intent(ViewMainActivity.this, ToolBarCollapsingToolbarLayoutActivity.class));
        }
    }
}
