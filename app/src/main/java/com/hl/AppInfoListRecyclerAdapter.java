package com.hl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 应用列表适配器
 */
public class AppInfoListRecyclerAdapter extends Adapter<AppInfoListRecyclerAdapter.Holder> {

    private List<PackageInfo> mPacks;
    private LayoutInflater inflater;
    private PackageManager mPackageManager;
    private Context mContext;

    public AppInfoListRecyclerAdapter(Context c) {
        this.inflater = LayoutInflater.from(c);
        this.mPackageManager = c.getPackageManager();
        this.mContext = c;
    }

    public void setData(List<PackageInfo> packs) {
        this.mPacks = packs;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_appinfolist, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final PackageInfo packageInfo = mPacks.get(position);
        holder.iv_icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(mPackageManager));
        holder.tv_name.setText(packageInfo.applicationInfo.loadLabel(mPackageManager));
        holder.tv_package_name.setText(packageInfo.packageName);


//        if( mOnItemClickListener!= null){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    mOnItemClickListener.onClick(position);
                Toast.makeText(mContext, "versionName: " + packageInfo.versionName + "\nversionCode: " + packageInfo.versionCode, Toast.LENGTH_SHORT).show();
            }
        });
//        }
    }

    @Override
    public int getItemCount() {
        return mPacks == null ? 0 : mPacks.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        ImageView iv_icon;
        TextView tv_name;
        TextView tv_package_name;

        public Holder(View view) {
            super(view);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_package_name = (TextView) view.findViewById(R.id.tv_package_name);

        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
