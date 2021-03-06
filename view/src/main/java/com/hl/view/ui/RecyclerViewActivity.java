package com.hl.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hl.view.R;
import com.hl.view.ViewBaseActivity;
import com.hl.view.ui.recycler.DeleteRecyclerActivity;
import com.hl.view.ui.recycler.StartRecyclerActivity;
import com.hl.view.ui.recycler.StartRecyclerFullActivity;

/**
 * RecyclerView 主页
 */
public class RecyclerViewActivity extends ViewBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_recycler_view);
        initToolbar(true);

        findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerViewActivity.this, DeleteRecyclerActivity.class));
            }
        });

        findViewById(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerViewActivity.this, SwipeRefreshRecyclerActivity.class));
            }
        });

        findViewById(R.id.tv_recycler_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerViewActivity.this, HeadRecyclerActivity.class));
            }
        });

        findViewById(R.id.tv_recycler_double).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerViewActivity.this, RecyclerDoubleActivity.class));
            }
        });

        findViewById(R.id.tv_quick_recycler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerViewActivity.this, QuickRecyclerActivity.class));
            }
        });

        findViewById(R.id.tv_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerViewActivity.this, MoveRecyclerActivity.class));
            }
        });

        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerViewActivity.this, StartRecyclerActivity.class));
            }
        });

        findViewById(R.id.tv_start_full).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerViewActivity.this, StartRecyclerFullActivity.class));
            }
        });
    }
}
