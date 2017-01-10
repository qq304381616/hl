package com.hl.design;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<String> mDatas;

    public RecyclerAdapter(Context context , List<String> datas) {
        inflater = LayoutInflater.from(context);
        this.mDatas = datas;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerAdapter.MyViewHolder holder = new RecyclerAdapter.MyViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent,
                false) );
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, final int position) {
        holder.tv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
