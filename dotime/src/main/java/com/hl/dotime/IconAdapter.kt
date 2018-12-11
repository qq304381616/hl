package com.hl.dotime

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hl.dotime.utils.ConstantIcon
import com.hl.dotime.utils.Utils

/**
 * Created by HL on 2018/5/18.
 */
class IconAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: List<String>? = null
    private var onItemClick: OnItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_icon, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as IconAdapter.Holder

        Utils.setSVGColor(mContext, viewHolder.iv_icon, ConstantIcon.map[mData!!.get(position)]!!, "#FFCCCCCC")

        viewHolder.itemView.setOnClickListener {
            if (onItemClick != null) {
                onItemClick!!.onItemClick(viewHolder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_icon)
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
    }
}