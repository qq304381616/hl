package com.hl.dotime.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hl.dotime.R
import com.hl.dotime.db.entity.TaskGroup

class TaskGroupAdapter(var c: Context) : BaseAdapter() {

    var data: List<TaskGroup>? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = View.inflate(c, R.layout.item_choose_group, null);
            holder = ViewHolder(convertView);
            convertView.tag = holder;
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.tv_name.text = getItem(position).name
        return convertView!!
    }

    override fun getItem(position: Int): TaskGroup {
        return data!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data?.size ?: 0
    }

    class ViewHolder(view: View) {
        var tv_name = view.findViewById<TextView>(R.id.tv_name)
    }

}