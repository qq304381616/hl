package com.hl.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.base.entity.BaseDataEntity;
import com.hl.utils.views.SideBar;
import com.hl.view.R;

import java.util.List;
import java.util.Locale;

/**
 * 右测字母定位
 */
public class QuickAdapter extends Adapter<RecyclerView.ViewHolder> implements SideBar.ScrollListener {

    private List<BaseDataEntity> mData;
    private LayoutInflater inflater;

    public QuickAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
    }

    public void setData(List<BaseDataEntity> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.base_item_recycler, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final BaseDataEntity data = mData.get(position);
        itemViewHolder.tv_name.setText(data.getInfo());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < mData.size(); i++) {
            String sortStr = mData.get(i).getFirst();
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;

        ItemViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
        }
    }
}
