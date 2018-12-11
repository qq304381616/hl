package com.hl.dotime.java;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by HL on 2018/5/17.
 */

public class TaskAdapterJava extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private String mData;

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        this.mData = data;
    }

    public TaskAdapterJava(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Holder viewHolder = (Holder) holder;
        viewHolder.textView.setText(mData);
        if (onItemClick != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(viewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    interface OnItemClick {
        void onItemClick(int position);
    }
}
