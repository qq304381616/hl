package com.hl.systeminfo.appinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hl.systeminfo.R;
import com.hl.utils.StringUtils;
import com.hl.utils.views.SideBar;

import java.util.List;
import java.util.Locale;

/**
 * 应用列表适配器
 */
public class AppInfoListRecyclerAdapter extends Adapter<RecyclerView.ViewHolder> implements SideBar.ScrollListener {

    private List<AppEntity> mPacks;
    private LayoutInflater inflater;
    private PackageManager mPackageManager;
    private Context mContext;
    private String searchText;

    public AppInfoListRecyclerAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mPackageManager = c.getPackageManager();
        this.mContext = c;
    }

    public void setData(List<AppEntity> packs) {
        this.mPacks = packs;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_appinfolist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final PackageInfo packageInfo = mPacks.get(position).getPackageInfo();
        itemViewHolder.iv_icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(mPackageManager));
        String name = packageInfo.applicationInfo.loadLabel(mPackageManager).toString();
        itemViewHolder.tv_name.setText(StringUtils.getColorString(name, searchText, ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
        itemViewHolder.tv_package_name.setText(StringUtils.getColorString(packageInfo.packageName, searchText, ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));

        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "versionName: " + packageInfo.versionName + "\nversionCode: " + packageInfo.versionCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPacks == null ? 0 : mPacks.size();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < mPacks.size(); i++) {
            String sortStr = mPacks.get(i).getFirst();
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_icon;
        TextView tv_name;
        TextView tv_package_name;

        ItemViewHolder(View view) {
            super(view);
            iv_icon = view.findViewById(R.id.iv_icon);
            tv_name = view.findViewById(R.id.tv_name);
            tv_package_name = view.findViewById(R.id.tv_package_name);
        }
    }
}
