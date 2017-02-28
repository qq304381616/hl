package com.hl.widget.recyclerdouble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InnerAdapter extends Adapter<InnerAdapter.Holder> {

    private List<String> mDatas;
    private LayoutInflater inflater;
    private Context mContext;

    public InnerAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public void setData(List<String> data) {
        this.mDatas = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final String data = mDatas.get(position);
        holder.tv_name.setText(data);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView tv_name;

        public Holder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(android.R.id.text1);
        }
    }

}
