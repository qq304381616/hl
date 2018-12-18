package com.hl.view.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.hl.base.BaseActivity;
import com.hl.base.BaseConstant;
import com.hl.view.R;
import com.hl.view.adapter.RefreshAdapter;

/**
 * RecyclerView + SwipeRefresh 实现下拉刷新 上拉加载更多
 */
public class SwipeRefreshRecyclerActivity extends BaseActivity {


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RefreshAdapter mRefreshAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_swiperefreshrecycler);
        initToolbar(true);

        mRecyclerView = findViewById(R.id.recycler);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mRefreshAdapter = new RefreshAdapter(this, BaseConstant.getData());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());   //添加动画
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));        //添加分割线
        mRecyclerView.setAdapter(mRefreshAdapter);

        initPullRefresh();
        initLoadMoreListener();
    }

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshAdapter.AddHeaderItem(BaseConstant.getData("Heard Item"));
                        mSwipeRefreshLayout.setRefreshing(false);   //刷新完成
                        Toast.makeText(SwipeRefreshRecyclerActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 2000);

            }
        });
    }

    private void initLoadMoreListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mRefreshAdapter.getItemCount()) {

                    //设置正在加载更多
                    mRefreshAdapter.changeMoreStatus(RefreshAdapter.LOADING_MORE);

                    //改为网络请求
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshAdapter.AddFooterItem(BaseConstant.getData("footer item"));
                            mRefreshAdapter.changeMoreStatus(RefreshAdapter.PULLUP_LOAD_MORE);   //设置回到上拉加载更多
                            //mRefreshAdapter.changeMoreStatus(mRefreshAdapter.NO_LOAD_MORE); //没有加载更多了
                            Toast.makeText(SwipeRefreshRecyclerActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();   //最后一个可见的ITEM
                }
            }
        });
    }
}
