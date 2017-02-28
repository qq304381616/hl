package com.hl.widget.recyclerdouble;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hl.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用列表适配器
 */
public class ParentAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<String> mDatas;
    private LayoutInflater inflater;
    private Context mContext;

    public ParentAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public void setData(List<String> datas) {
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_parent, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final String finds = mDatas.get(position);
        itemViewHolder.tv_name.setText(finds);

        itemViewHolder.recycler_inner.setLayoutManager(new LinearLayoutManager(mContext));
        InnerAdapter innerAdapter = new InnerAdapter(mContext);
        List<String> datas = new ArrayList<String>();
        datas.add("aaa");
        datas.add("bbb");
        datas.add("ccc");
        datas.add("ddd");
        datas.add("ccc");
        innerAdapter.setData(datas);
        itemViewHolder.recycler_inner.setAdapter(innerAdapter);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        RecyclerView recycler_inner;

        public ItemViewHolder(View view) {
            super(view);
            recycler_inner = (RecyclerView) view.findViewById(R.id.recycler_inner);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
