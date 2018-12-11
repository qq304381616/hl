package com.hl.dotime.ui.filter

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import com.hl.dotime.R
import com.hl.dotime.adapter.FilterPagerAdapter
import com.hl.dotime.base.BaseActivity
import com.hl.dotime.db.entity.FilterList
import com.hl.dotime.db.service.TaskRecordService
import com.hl.dotime.entity.FilterCount
import com.hl.dotime.utils.DateUtils
import java.util.*

class FilterCountActivity : BaseActivity(), View.OnClickListener {

    private lateinit var taskRecordService: TaskRecordService
    private lateinit var btn_pre: Button
    private lateinit var btn_type: Button
    private lateinit var btn_next: Button
    private var type = 2
    private val typeArray = arrayOf("日", "周", "月", "季", "年")
    private var filter = FilterCount(-1, -1, "")
    private var pageNum = 0
    private lateinit var listFragment: ListFilterFragment
    private lateinit var typeCountFragment: TypeCountFragment
    private lateinit var chartFragment: ChartFragment
    private var data: ArrayList<FilterList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_count)

        initToolbar()

        val tablayout = findViewById<TabLayout>(R.id.tl_tab)
        tablayout.setSelectedTabIndicatorColor(resources.getColor(R.color.app_color))
        tablayout.setSelectedTabIndicatorHeight(8)
        tablayout.setTabTextColors(Color.BLACK, resources.getColor(R.color.app_color))

        val viewpager = findViewById<ViewPager>(R.id.vp_content)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                pageNum = p0
                updateData()
            }

            override fun onPageScrollStateChanged(p0: Int) {
            }
        })
        val adapter = FilterPagerAdapter(supportFragmentManager)
        listFragment = ListFilterFragment()
        typeCountFragment = TypeCountFragment()
        chartFragment = ChartFragment()
        adapter.addFragment(listFragment, "列表")
        adapter.addFragment(typeCountFragment, "分类统计")
        adapter.addFragment(chartFragment, "图表")
        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)

        btn_pre = findViewById(R.id.btn_pre)
        btn_type = findViewById(R.id.btn_type)
        btn_next = findViewById(R.id.btn_next)
        btn_pre.setOnClickListener(this)
        btn_type.setOnClickListener(this)
        btn_next.setOnClickListener(this)

        taskRecordService = TaskRecordService(this)

        initData()
    }

    private fun initData() {
        getFilterTime(-1, 0)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_pre -> {
                getFilterTime(filter.start, -1)
            }
            R.id.btn_type -> {
                showSelectTypeDialog()
            }
            R.id.btn_next -> {
                getFilterTime(filter.start, 1)
            }
        }
    }

    /**
     * 选择菜单
     */
    private fun showSelectTypeDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("请选择")
        dialog.setSingleChoiceItems(typeArray, type) { d, i ->
            type = i
            initData()
            d.dismiss()
        }
        dialog.setIcon(R.mipmap.ic_launcher_round)
        dialog.create().show()
    }

    fun getData(): ArrayList<FilterList>? {
        return data
    }

    private fun updateData() {
        btn_type.text = filter.text
        data = taskRecordService.queryByTime(filter.start.toString(), filter.end.toString())
        when (pageNum) {
            1 -> {
                typeCountFragment.updateData(data)
            }
            2 -> {
                chartFragment.updateData(data)
            }
            else -> {
                listFragment.updateData(data)
            }
        }
    }

    private fun getFilterTime(curr: Long, step: Int) {
        filter = DateUtils.getFilterTime(type, curr, step)
        Log.e("TAG", DateUtils.getYMDHMS(filter.start))
        Log.e("TAG", DateUtils.getYMDHMS(filter.end))
        updateData()
    }
}