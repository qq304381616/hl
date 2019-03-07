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
import com.hl.view.ui.pip.PipActivity;
import com.hl.view.ui.toolbar.ToolBarMainActivity;

/**
 * 控件集合
 */
public class ViewMainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_view_main);
        initToolbar(true);

        findViewById(R.id.tv_easy).setOnClickListener(this);
        findViewById(R.id.tv_layout).setOnClickListener(this);
        findViewById(R.id.tv_toolbar).setOnClickListener(this);
        findViewById(R.id.tv_recycler).setOnClickListener(this);
        findViewById(R.id.tv_pip).setOnClickListener(this);
        findViewById(R.id.tv_dialog).setOnClickListener(this);
        findViewById(R.id.tv_SnackBar).setOnClickListener(this);
        findViewById(R.id.tv_page_sliding_tab).setOnClickListener(this);
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
        } else if (id == R.id.tv_recycler) {
            startActivity(new Intent(ViewMainActivity.this, RecyclerViewActivity.class));
        } else if (id == R.id.tv_pip) { // 画中画
            startActivity(new Intent(ViewMainActivity.this, PipActivity.class));
        } else if (id == R.id.tv_easy) {
            startActivity(new Intent(ViewMainActivity.this, WidgetActivity.class));
        } else if (id == R.id.tv_toolbar) {
            startActivity(new Intent(ViewMainActivity.this, ToolBarMainActivity.class));
        } else if (id == R.id.tv_layout) {
            startActivity(new Intent(ViewMainActivity.this, LayoutActivity.class));
        } else if (id == R.id.tv_dialog) {
            startActivity(new Intent(ViewMainActivity.this, DialogMainActivity.class));
        } else if (id == R.id.tv_page_sliding_tab) {
            Intent intent = new Intent(ViewMainActivity.this, com.hl.tab.ui.activity.MainActivity.class);
            startActivity(intent);
        }
    }
}
