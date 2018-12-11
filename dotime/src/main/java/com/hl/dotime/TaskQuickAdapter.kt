package com.hl.dotime

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hl.dotime.db.entity.Task
import com.hl.dotime.utils.ConstantIcon
import com.hl.dotime.utils.Utils

/**
 * Created by HL on 2018/5/18.
 */
class TaskQuickAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: ArrayList<Task>? = null
    private var onItemClick: TaskQuickAdapter.OnItemClick? = null
    private var onItemLongClick: OnItemLongClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_task_quick, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as TaskQuickAdapter.Holder
        val task = mData!![position]
        Utils.setSVGColor(mContext, viewHolder.iv_icon, ConstantIcon.map[task.iconName]!!, task.iconColor!!)
        viewHolder.tv_name.text = task.name
        if (onItemClick != null) {
            viewHolder.itemView.setOnClickListener { onItemClick!!.onItemClick(viewHolder.adapterPosition) }
        }
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val iv_icon: ImageView = itemView.findViewById(R.id.iv_icon)
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
    }

    fun setOnItemLongClick(onItemLongClick: OnItemLongClick) {
        this.onItemLongClick = onItemLongClick
    }

    interface OnItemLongClick {
        fun onItemLongClick(position: Int)
    }

    fun insertItem(position: Int, item: Task) {
        mData!!.add(position, item)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        mData!!.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
}