package com.hl.dotime

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hl.dotime.db.entity.TaskRecord
import com.hl.dotime.utils.ConstantIcon
import com.hl.dotime.utils.DateUtils
import com.hl.dotime.utils.Utils
import java.util.*

/**
 * Created by HL on 2018/5/18.
 */
class TaskCountAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: ArrayList<TaskRecord>? = null
    private var onItemClick: OnItemClick? = null
    private var onItemLongClick: OnItemLongClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_count, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as TaskCountAdapter.Holder
        val task = mData!![position]

        Utils.setSVGColor(mContext, viewHolder.iv_icon, ConstantIcon.map[task.task!!.iconName]!!, task.task!!.iconColor!!)

        viewHolder.tv_name.text = task.name.toString()
        viewHolder.tv_describe.text = task.mark!!.name

        val timerList = task.timerList
        var before: Long = 0 // 之前时间统计之合
        for (i in timerList!!.indices) {
            if (timerList.get(i).endTime != null && timerList.get(i).endTime != 0L) {
                before += timerList.get(i).endTime!! - timerList.get(i).startTime!!
            }
        }
        viewHolder.tv_time.text = DateUtils.getTime(before)
        if (timerList.size != 0) {
            viewHolder.tv_date.text = DateUtils.getYMD(timerList.get(0).startTime!!)
        }

        viewHolder.itemView.setOnClickListener {
            onItemClick?.onItemClick(position)
        }

        viewHolder.itemView.setOnLongClickListener {
            onItemLongClick?.onItemLongClick(position)
            true
        }
    }

    fun updateItem(item: TaskRecord) {
        for (i in mData!!.indices) {
            if (item.id == mData!!.get(i).id) {
                mData!!.set(i, item)
                notifyItemChanged(i)
                break
            }
        }
    }

    fun insertItem(position: Int, item: TaskRecord) {
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
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val tv_describe: TextView = itemView.findViewById(R.id.tv_describe)
        val tv_date: TextView = itemView.findViewById(R.id.tv_date)
        val tv_time: TextView = itemView.findViewById(R.id.tv_time)
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_icon)
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    interface OnItemLongClick {
        fun onItemLongClick(position: Int)
    }

    fun setOnItemLongClick(onItemLongClick: OnItemLongClick) {
        this.onItemLongClick = onItemLongClick
    }
}