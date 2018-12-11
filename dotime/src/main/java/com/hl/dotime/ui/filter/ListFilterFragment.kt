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
import com.hl.dotime.adapter.ListFilterAdapter
import com.hl.dotime.db.entity.FilterList
import java.util.*

class ListFilterFragment : Fragment() {

    private var listFilterAdapter: ListFilterAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_list_filter, null)

        val rv_filter_list = contentView.findViewById<RecyclerView>(R.id.rv_filter_list)
        rv_filter_list.itemAnimator = DefaultItemAnimator()
        rv_filter_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_filter_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        listFilterAdapter = ListFilterAdapter(activity!!)
        rv_filter_list.adapter = listFilterAdapter

        val a = activity as FilterCountActivity
        updateData(a.getData())

        return contentView
    }

    fun updateData(data: ArrayList<FilterList>?) {
        listFilterAdapter?.mData = data
        listFilterAdapter?.notifyDataSetChanged()
    }
}