package com.hl.dotime.ui.filter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hl.dotime.R
import com.hl.dotime.adapter.TypeCountAdapter
import com.hl.dotime.db.entity.FilterList
import java.util.*

// 分类统计
class TypeCountFragment : Fragment() {

    private lateinit var typeCountAdapter: TypeCountAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_list_filter, null)

        val rv_filter_list = contentView.findViewById<RecyclerView>(R.id.rv_filter_list)
        rv_filter_list.itemAnimator = DefaultItemAnimator()
        rv_filter_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_filter_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        typeCountAdapter = TypeCountAdapter(activity!!)
        rv_filter_list.adapter = typeCountAdapter

        val a = activity as FilterCountActivity
        updateData(a.getData())

        return contentView
    }

    fun updateData(data: ArrayList<FilterList>?) {
        typeCountAdapter.mData = handleData(data)
        typeCountAdapter.notifyDataSetChanged()
    }

    fun handleData(data: ArrayList<FilterList>?): ArrayList<Category>? {
        if (data == null) return null
        val map = hashMapOf<String, Category>()
        var countTime: Long = 0 // 总时间
        for (j in data) {
            val before = j.endTime!! - j.startTime!!
            countTime += before
            if (map.containsKey(j.taskId)) {
                map.get(j.taskId!!)!!.time = before + map.get(j.taskId!!)!!.time
            } else {
                map.put(j.taskId!!, Category(j.name!!, j.iconName!!, j.iconColor!!, before, 0f))
            }
        }

        val list = ArrayList<Category>()

        for (i in map.values) {
            if (countTime == 0L) {
                i.percent = 0f
            } else {
                i.percent = String.format("%.2f", i.time * 100f / countTime).toFloat() // 格式化保留两位小数
            }
            list.add(i)
        }

        list.sortBy { it.percent }
        list.reverse()
        return list
    }

    class Category(var taskName: String, var taskIconName: String, var taskIconColor: String, var time: Long, var percent: Float)
}