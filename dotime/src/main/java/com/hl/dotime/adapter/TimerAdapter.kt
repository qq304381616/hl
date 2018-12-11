package com.hl.dotime.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hl.dotime.R
import com.hl.dotime.db.entity.RecordTimer
import com.hl.dotime.utils.DateUtils

/**
 * Created by HL on 2018/5/18.
 */
class TimerAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: java.util.ArrayList<RecordTimer>? = null
    private var onItemClick: OnItemClick? = null
    private var onItemLongClick: OnItemLongClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_timer, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as Holder
        val task = mData!![position]
        if (task.endTime == null || task.endTime == 0L) {
            viewHolder.tv_count.text = DateUtils.getTime(DateUtils.getSeconds() - task.startTime!!)
            viewHolder.tv_time.text = DateUtils.getYMDHMS(task.startTime!!) + "    -    " + "进行中..."
        } else {
            viewHolder.tv_count.text = DateUtils.getTime(task.endTime!! - task.startTime!!)
            viewHolder.tv_time.text = DateUtils.getYMDHMS(task.startTime!!) + "    -    " + DateUtils.getYMDHMS(task.endTime!!)
        }

        viewHolder.itemView.setOnClickListener {
            onItemClick?.onItemClick(position)
        }

        viewHolder.itemView.setOnLongClickListener {
            onItemLongClick?.onItemLongClick(position)
            true
        }
    }

    fun insertItem(position: Int, item: RecordTimer) {
        mData!!.add(position, item)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        mData!!.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_time: TextView = itemView.findViewById(R.id.tv_time)
        val tv_count: TextView = itemView.findViewById(R.id.tv_count)
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setOnItemLongClick(onItemLongClick: OnItemLongClick) {
        this.onItemLongClick = onItemLongClick
    }

    interface OnItemLongClick {
        fun onItemLongClick(position: Int)
    }
}
