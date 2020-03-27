package com.hl.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.base.IBaseCallback;
import com.hl.base.R;
import com.hl.base.entity.BaseDataEntity;

import java.util.List;

/**
 * 全屏
 */
public class StartFullRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseDataEntity> data;
    private LayoutInflater inflater;
    private IBaseCallback mCallback;

    public StartFullRecyclerAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
    }

    public List<BaseDataEntity> getData() {
        return data;
    }

    public void setData(List<BaseDataEntity> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.base_item_recycler_full, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        BaseDataEntity d = data.get(holder.getAdapterPosition());
        itemHolder.tv_name.setText(d.getInfo());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView tv_name;

        ItemHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
        }
    }

    public void setCallback(IBaseCallback callback) {
        this.mCallback = callback;
    }
}
