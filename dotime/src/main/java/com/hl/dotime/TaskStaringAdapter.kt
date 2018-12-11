package com.hl.dotime

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
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
class TaskStaringAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: ArrayList<TaskRecord>? = null
    private var onItemClick: OnItemClick? = null
    private var onItemLongClick: OnItemLongClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_task, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as TaskStaringAdapter.Holder
        val task = mData!![position]

        Utils.setSVGColor(mContext, viewHolder.iv_icon, ConstantIcon.map[task.task!!.iconName]!!, task.task!!.iconColor!!)
        viewHolder.tv_name.text = task.name.toString()

        val timerList = task.timerList
        var before: Long = 0 // 之前时间统计之合
        for (i in timerList!!.indices) {
            if (timerList[i].endTime != null && timerList[i].endTime != 0L) {
                before += timerList[i].endTime!! - timerList[i].startTime!!
            }
        }
        if (task.status == 1) {
            viewHolder.tv_start_stop.setImageResource(R.drawable.icon_pause)
            val time = DateUtils.getSeconds() - timerList[timerList.size - 1].startTime!! + before
            viewHolder.tv_time.text = DateUtils.getTime(time)
        } else {
            viewHolder.tv_start_stop.setImageResource(R.drawable.icon_play)
            viewHolder.tv_time.text = DateUtils.getTime(before)
        }

        viewHolder.tv_start_stop.setOnClickListener {
            if (onItemClick != null) {
                onItemClick!!.onStartOrStop(viewHolder.adapterPosition)
            }
        }

        viewHolder.tv_stop.setOnClickListener {
            if (onItemClick != null) {
                onItemClick!!.onStopClick(viewHolder.adapterPosition)
            }
            removeItem(position)
        }

        viewHolder.itemView.setOnClickListener {
            if (onItemClick != null) {
                onItemClick!!.onItemClick(viewHolder.adapterPosition)
            }
        }
        viewHolder.itemView.setOnLongClickListener() {
            if (onItemLongClick != null) {
                onItemLongClick!!.onItemLongClick(viewHolder.adapterPosition)
            }
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
        val tv_time: TextView = itemView.findViewById(R.id.tv_time)
        val tv_start_stop: ImageView = itemView.findViewById(R.id.tv_start_stop)
        val tv_stop: ImageView = itemView.findViewById(R.id.tv_stop)
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_icon)
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
        fun onStartOrStop(position: Int)
        fun onStopClick(position: Int)
    }

    fun setOnItemLongClick(onItemLongClick: OnItemLongClick) {
        this.onItemLongClick = onItemLongClick
    }

    interface OnItemLongClick {
        fun onItemLongClick(position: Int)
    }
}