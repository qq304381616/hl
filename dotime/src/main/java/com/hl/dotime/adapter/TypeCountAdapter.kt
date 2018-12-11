package com.hl.dotime.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hl.dotime.R
import com.hl.dotime.ui.filter.TypeCountFragment
import com.hl.dotime.utils.ConstantIcon
import com.hl.dotime.utils.DateUtils
import com.hl.dotime.utils.Utils
import java.util.*

class TypeCountAdapter(var mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: ArrayList<TypeCountFragment.Category>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_type_count, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as Holder
        val category = mData!!.get(position)

        Utils.setSVGColor(mContext, viewHolder.iv_icon, ConstantIcon.map[category.taskIconName]!!, category.taskIconColor!!)

        viewHolder.tv_name.text = category.percent.toString() + "%    " + category.taskName + "    " + DateUtils.getTime(category.time)
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_icon)
    }
}