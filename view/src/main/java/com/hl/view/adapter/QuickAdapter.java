package com.hl.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.view.R;
import com.hl.view.entity.SortModel;

import java.util.List;
import java.util.Locale;

/**
 * 应用列表适配器
 */
public class QuickAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<SortModel> mDatas;
    private LayoutInflater inflater;
    private Context mContext;

    public QuickAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public void setData(List<SortModel> datas) {
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.view_item_parent, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final SortModel data = mDatas.get(position);
        itemViewHolder.tv_name.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;


        public ItemViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < mDatas.size(); i++) {
            String sortStr = mDatas.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }
}
