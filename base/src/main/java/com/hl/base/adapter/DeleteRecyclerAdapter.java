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
import com.hl.base.view.swipeRecyclerView.SwipeItemLayout;

import java.util.List;

/**
 * 滑动删除
 */
public class DeleteRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseDataEntity> data;
    private LayoutInflater inflater;
    private IBaseCallback mCallback;

    public DeleteRecyclerAdapter(Context c) {
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
        View itemView = inflater.inflate(R.layout.base_delete_item_recycler, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.mSwipeItemLayout.setSwipeEnable(true);
        BaseDataEntity d = data.get(holder.getAdapterPosition());
        itemHolder.tv_name.setText(d.getInfo());

        itemHolder.mRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.callback(itemHolder.getAdapterPosition());
                    itemHolder.mSwipeItemLayout.close();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        View mRightMenu;
        SwipeItemLayout mSwipeItemLayout;

        ItemHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            mRightMenu = itemView.findViewById(R.id.right_menu);
            mSwipeItemLayout = itemView.findViewById(R.id.swipe_layout);
        }
    }

    public void setCallback(IBaseCallback callback) {
        this.mCallback = callback;
    }
}
