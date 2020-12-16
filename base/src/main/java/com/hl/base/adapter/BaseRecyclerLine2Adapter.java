package com.hl.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.base.R;
import com.hl.base.entity.BaseDataEntity;

import java.util.List;

/**
 * 基础适配器
 */
public class BaseRecyclerLine2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseDataEntity> data;
    private LayoutInflater inflater;
    private Context mContext;

    public BaseRecyclerLine2Adapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
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
        View itemView = inflater.inflate(R.layout.base_item_recycler_line2, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        BaseDataEntity d = data.get(holder.getAdapterPosition());
        itemHolder.tv_name.setText(d.getInfo());
        itemHolder.tv_desc.setText(d.getDesc());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_desc;

        ItemHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_desc = view.findViewById(R.id.tv_desc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
