package com.hl.dotime.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hl.dotime.R
import com.hl.dotime.db.entity.FilterList
import com.hl.dotime.utils.ConstantIcon
import com.hl.dotime.utils.DateUtils
import com.hl.dotime.utils.Utils

/**
 * Created by HL on 2018/5/18.
 */
class ListFilterAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: java.util.ArrayList<FilterList>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_list_filter, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as Holder
        val filter = mData!![position]
        Utils.setSVGColor(mContext, viewHolder.iv_icon, ConstantIcon.map[filter.iconName]!!, filter.iconColor!!)
        viewHolder.tv_name.text = filter.name
        viewHolder.tv_describe.text = filter.mark
        viewHolder.tv_describe.text = filter.mark
        viewHolder.tv_date.text = DateUtils.getYMD(filter.startTime!!)
        viewHolder.tv_time.text = DateUtils.getTime(filter.endTime!! - filter.startTime!!)
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_icon)
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val tv_describe: TextView = itemView.findViewById(R.id.tv_describe)
        val tv_date: TextView = itemView.findViewById(R.id.tv_date)
        val tv_time: TextView = itemView.findViewById(R.id.tv_time)
    }
}
