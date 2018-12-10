package com.hl.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.view.R;

import java.util.List;

/**
 * 带头布局的 recycler adapter
 */
public class HeadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mData;

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;

    public List<String> getData() {
        return mData;
    }

    public void setData(List<String> mData) {
        this.mData = mData;
    }

    public HeadAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View itemView = mInflater.inflate(R.layout.view_item_head_head, parent, false);
            return new HeadViewHolder(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.view_item_head_item, parent, false);
            return new ItemViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            headViewHolder.tv_head.setText("head_view");

        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            String s = mData.get(position - 1);
            itemViewHolder.tv_item.setText(s);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 1 : mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_ITEM;
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {

        TextView tv_head;

        HeadViewHolder(View itemView) {
            super(itemView);
            tv_head = (TextView) itemView.findViewById(R.id.tv_head);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item;

        ItemViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
