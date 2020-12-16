package com.hl.dotime

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.hl.dotime.base.BaseActivity
import com.hl.dotime.ui.TaskDetailsActivity
import com.hl.dotime.ui.filter.FilterCountActivity
import java.util.*

/**
 * 主页
 * Created by HL on 2018/5/16.
 */
class HomeActivity : BaseActivity() {

    private var mTabTl: TabLayout? = null
    private var mContentVp: ViewPager? = null
    private var toolbar: Toolbar? = null

    private var tabIndicators: MutableList<String>? = null
    private var tabFragments: MutableList<Fragment>? = null
    private var contentAdapter: ContentPagerAdapter? = null

    private var task: TaskFragment? = null
    private var taskManager: TaskManagerFragment? = null
    private var count: CountFragment? = null
    private var me: MeFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mTabTl = findViewById(R.id.tl_tab)
        mContentVp = findViewById(R.id.vp_content)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true) //设置返回键可用
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP

        task = TaskFragment.newInstance("task")
        taskManager = TaskManagerFragment.newInstance("task_manager")
        count = CountFragment.newInstance("count")
        me = MeFragment.newInstance("me")

        initContent()

        mTabTl!!.setSelectedTabIndicatorHeight(0)
        mTabTl!!.setupWithViewPager(mContentVp)
        for (i in tabIndicators!!.indices) {
            val itemTab = mTabTl!!.getTabAt(i)
            itemTab!!.setCustomView(R.layout.layout_tab)
        }

        initTab(0)

        mContentVp!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                initTab(position)
                invalidateOptionsMenu();
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // 动态设置ToolBar状态
        menu.findItem(R.id.add_task).isVisible = false
        menu.findItem(R.id.filter).isVisible = false
        when (mContentVp!!.currentItem) {
            0 -> {
                menu.findItem(R.id.add_task).isVisible = true
            }
            1 -> {
            }
            2 -> {
                menu.findItem(R.id.filter).isVisible = true
            }
            3 -> {
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu); // 暂不显示菜单
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_task -> {
                // 增加计时任务
                val intent = Intent(this, TaskDetailsActivity::class.java)
                startActivity(intent)
            }
            // 筛选
            R.id.filter -> {
                val intent = Intent(this, FilterCountActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initTab(position: Int) {
        for (i in tabIndicators!!.indices) {
            val itemTab = mTabTl!!.getTabAt(i)
            val itemTv = itemTab!!.customView!!.findViewById<TextView>(R.id.tv_menu_item)
            itemTv.text = tabIndicators!![i]
            itemTv.setTextColor(resources.getColor(R.color.app_default))
            val iv_icon = itemTab!!.customView!!.findViewById<ImageView>(R.id.iv_icon)
            when (i) {
                0 -> {
                    iv_icon.setBackgroundResource(R.drawable.icon_task_management)
                    if (position == 0) {
                        iv_icon.setBackgroundResource(R.drawable.icon_task_management_pre)
                        itemTv.setTextColor(resources.getColor(R.color.app_color))
                    }
                }
                1 -> {
                    iv_icon.setBackgroundResource(R.drawable.icon_set)
                    if (position == 1) {
                        iv_icon.setBackgroundResource(R.drawable.icon_set_pre)
                        itemTv.setTextColor(resources.getColor(R.color.app_color))
                    }
                }
                2 -> {
                    iv_icon.setBackgroundResource(R.drawable.icon_integral)
                    if (position == 2) {
                        iv_icon.setBackgroundResource(R.drawable.icon_integral_pre)
                        itemTv.setTextColor(resources.getColor(R.color.app_color))
                    }
                }
                3 -> {
                    iv_icon.setBackgroundResource(R.drawable.icon_add)
                    if (position == 3) {
                        iv_icon.setBackgroundResource(R.drawable.icon_add_pre)
                        itemTv.setTextColor(resources.getColor(R.color.app_color))
                    }
                }
            }
        }
    }

    private fun initContent() {
        tabIndicators = ArrayList()
        tabIndicators!!.add("任务")
        tabIndicators!!.add("管理")
        tabIndicators!!.add("统计")
        tabIndicators!!.add("我的")
        tabFragments = ArrayList()
        tabFragments!!.add(task!!)
        tabFragments!!.add(taskManager!!)
        tabFragments!!.add(count!!)
        tabFragments!!.add(me!!)

        contentAdapter = ContentPagerAdapter(supportFragmentManager)
        mContentVp!!.adapter = contentAdapter
    }

    internal inner class ContentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return tabFragments!![position]
        }

        override fun getCount(): Int {
            return tabIndicators!!.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabIndicators!![position]
        }
    }
}