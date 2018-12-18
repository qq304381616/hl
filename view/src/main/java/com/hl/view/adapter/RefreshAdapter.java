package com.hl.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hl.base.entity.BaseDataEntity;
import com.hl.view.R;

import java.util.List;

/**
 * 下拉刷新 上拉加载
 */
public class RefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BaseDataEntity> mData;
    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;

    public RefreshAdapter(Context context, List<BaseDataEntity> data) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.base_item_recycler, parent, false);
            return new ItemViewHolder(itemView);
        } else {
            View itemView = mInflater.inflate(R.layout.view_load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            BaseDataEntity data = mData.get(position);
            itemViewHolder.tv_name.setText(data.getInfo());

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    footerViewHolder.mTvLoadText.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footerViewHolder.mTvLoadText.setText("正加载更多...");
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;  //最后一个item设置为footerView
        } else {
            return TYPE_ITEM;
        }
    }

    public void AddHeaderItem(List<BaseDataEntity> items) {
        mData.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<BaseDataEntity> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 更新加载更多状态
     */
    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;

        ItemViewHolder(View itemView) {
            super(itemView);
            initListener(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

        private void initListener(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        ProgressBar mPbLoad;
        TextView mTvLoadText;
        LinearLayout mLoadLayout;

        FooterViewHolder(View itemView) {
            super(itemView);
            mPbLoad = itemView.findViewById(R.id.pbLoad);
            mTvLoadText = itemView.findViewById(R.id.tvLoadText);
            mLoadLayout = itemView.findViewById(R.id.loadLayout);

        }
    }
}
