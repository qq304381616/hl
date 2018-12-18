package com.hl.view.ui.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hl.base.BaseConstant;
import com.hl.base.IBaseCallback;
import com.hl.base.adapter.DeleteRecyclerAdapter;
import com.hl.utils.ToastUtils;
import com.hl.view.R;
import com.hl.view.ViewBaseActivity;

/**
 * 滑动删除
 */
public class DeleteRecyclerActivity extends ViewBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_delete_recycler);
        initToolbar(true);

        RecyclerView rv_base = findViewById(R.id.rv_base);

        rv_base.setLayoutManager(new LinearLayoutManager(this));
        DeleteRecyclerAdapter adapter = new DeleteRecyclerAdapter(this);
        rv_base.setAdapter(adapter);
        rv_base.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setCallback(new IBaseCallback() {
            @Override
            public void callback(int position) {
                ToastUtils.showShortToast(getApplicationContext(), "点击删除");
            }
        });
        adapter.setData(BaseConstant.getData());
        adapter.notifyDataSetChanged();
    }
}
