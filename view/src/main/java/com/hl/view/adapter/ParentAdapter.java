package com.hl.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.base.BaseConstant;
import com.hl.base.adapter.BaseRecyclerAdapter;
import com.hl.base.entity.BaseDataEntity;
import com.hl.view.R;

import java.util.List;

/**
 * 嵌套父布局
 */
public class ParentAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<BaseDataEntity> mData;
    private LayoutInflater inflater;
    private Context mContext;

    public ParentAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public void setData(List<BaseDataEntity> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.view_item_parent, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final BaseDataEntity data = mData.get(position);
        itemViewHolder.tv_name.setText(data.getInfo());

        itemViewHolder.recycler_inner.setLayoutManager(new LinearLayoutManager(mContext));
        itemViewHolder.recycler_inner.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        BaseRecyclerAdapter innerAdapter = new BaseRecyclerAdapter(mContext);

        innerAdapter.setData(BaseConstant.getData("child", 5));
        itemViewHolder.recycler_inner.setAdapter(innerAdapter);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        RecyclerView recycler_inner;

        ItemViewHolder(View view) {
            super(view);
            recycler_inner = view.findViewById(R.id.recycler_inner);
            tv_name = view.findViewById(R.id.tv_name);
        }
    }
}
