package com.hl.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.base.R;
import com.hl.base.entity.BaseDataEntity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多选适配器
 */
public class CheckBoxRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseDataEntity> data;
    private LayoutInflater inflater;
    private Context mContext;
    private ItemClickListener itemClickListener;
    private String current = "/"; // 当前目录
    private Map<String, Map<Integer, String>> pathChoose = new HashMap<>();
    private Map<Integer, String> choose;

    public CheckBoxRecyclerAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public List<BaseDataEntity> getData() {
        return data;
    }

    public void setData(List<BaseDataEntity> data) {
        this.data = data;
    }

    public void setDataAndCurrent(List<BaseDataEntity> data, String current) {
        this.data = data;
        this.current = current;
        if (pathChoose.containsKey(this.current)) {
            this.choose = pathChoose.get(this.current);
        } else {
            this.choose = new HashMap<>();
            pathChoose.put(this.current, this.choose);
        }
    }

    public Map<String, Map<Integer, String>> getPathChoose() {
        return pathChoose;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.base_item_recycler_checkbox, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        final BaseDataEntity item = data.get(holder.getAdapterPosition());
        itemHolder.tv_name.setText(item.getInfo());

        if (new File(item.getDesc()).isDirectory()) {
            itemHolder.cb_check.setVisibility(View.GONE);
            itemHolder.iv_more.setVisibility(View.VISIBLE);
            itemHolder.iv_icon.setBackgroundResource(R.drawable.icon_folder);
        } else {
            itemHolder.cb_check.setVisibility(View.VISIBLE);
            itemHolder.iv_more.setVisibility(View.GONE);
            itemHolder.iv_icon.setBackgroundResource(R.drawable.icon_file);
        }

        itemHolder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (choose == null) {
                    choose = new HashMap<>();
                }
                if (b) {
                    choose.put(itemHolder.getAdapterPosition(), item.getDesc());
                } else {
                    choose.remove(itemHolder.getAdapterPosition());
                }
                if (itemClickListener != null) {
                    itemClickListener.onItemChoose();
                }
            }
        });

        if (choose != null && choose.containsKey(position)) {
            itemHolder.cb_check.setChecked(true);
        } else {
            itemHolder.cb_check.setChecked(false);
        }

        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = new File(item.getDesc());
                if (f.isDirectory()) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(itemHolder.getAdapterPosition());
                    }
                } else {
                    if (choose != null && choose.containsKey(itemHolder.getAdapterPosition())) {
                        itemHolder.cb_check.setChecked(false);
                    } else {
                        itemHolder.cb_check.setChecked(true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);

        void onItemChoose();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        CheckBox cb_check;
        ImageView iv_more;
        ImageView iv_icon;

        ItemHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            cb_check = view.findViewById(R.id.cb_check);
            iv_more = view.findViewById(R.id.iv_more);
            iv_icon = view.findViewById(R.id.iv_icon);
        }
    }
}
