package com.hl.systeminfo.contact;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hl.base.entity.BaseDataEntity;
import com.hl.systeminfo.R;
import com.hl.utils.StringUtils;
import com.hl.utils.views.SideBar;

import java.util.List;
import java.util.Locale;

/**
 * 通讯录
 */
public class ContactAdapter extends Adapter<RecyclerView.ViewHolder> implements SideBar.ScrollListener {

    private List<BaseDataEntity> mData;
    private LayoutInflater inflater;
    private String searchText;
    private Context mContext;

    public ContactAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        mContext = c;
    }

    public void setData(List<BaseDataEntity> data) {
        this.mData = data;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
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
        itemViewHolder.tv_name.setText(StringUtils.getColorString(data.getInfo(), searchText, ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
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
