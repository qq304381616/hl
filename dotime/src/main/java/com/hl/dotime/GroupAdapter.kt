package com.hl.dotime

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hl.dotime.db.entity.TaskGroup
import java.util.*

/**
 * Created by HL on 2018/5/18.
 */
class GroupAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: ArrayList<TaskGroup>? = null
    private var onItemClick: OnItemClick? = null
    private var onItemLongClick: OnItemLongClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_group, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as GroupAdapter.Holder
        val task = mData!![position]
        viewHolder.tv_name.text = task.name.toString()

        viewHolder.itemView.setOnClickListener {
            if (onItemClick != null) {
                onItemClick!!.onItemClick(position)
            }
        }
        viewHolder.itemView.setOnLongClickListener {
            if (onItemLongClick != null) {
                onItemLongClick!!.onItemLongClick(position)
            }
            true
        }
    }

    fun insertItem(position: Int, item: TaskGroup) {
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
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setOnItemLongClick(onItemClick: OnItemLongClick) {
        this.onItemLongClick = onItemClick
    }

    interface OnItemLongClick {
        fun onItemLongClick(position: Int)
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
    }
}