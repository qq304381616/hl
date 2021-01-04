package com.hl.dotime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hl.dotime.db.entity.TaskRecord
import com.hl.dotime.db.service.TaskRecordService
import com.hl.dotime.ui.TaskDetailsActivity

/**
 * 页3  统计页面。
 */
class CountFragment : Fragment() {

    private lateinit var taskCountAdapter: TaskCountAdapter
    private lateinit var tv_loading: TextView
    private lateinit var taskRecordService: TaskRecordService
    private lateinit var receiver: MyReceiver
    private lateinit var intentFilter: IntentFilter

    companion object {
        private val EXTRA_CONTENT = "content"

        fun newInstance(content: String): CountFragment {
            val arguments = Bundle()
            arguments.putString(EXTRA_CONTENT, content)
            val tabContentFragment = CountFragment()
            tabContentFragment.arguments = arguments
            return tabContentFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity!!.unregisterReceiver(receiver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_count, container, false)

        receiver = MyReceiver()
        intentFilter = IntentFilter()
        intentFilter.addAction("task_record")
        activity!!.registerReceiver(receiver, intentFilter)

        tv_loading = contentView.findViewById(R.id.tv_loading)
        val rv_count_list = contentView.findViewById<RecyclerView>(R.id.rv_count_list)
        val mLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_count_list.itemAnimator = DefaultItemAnimator()
        rv_count_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_count_list.layoutManager = mLinearLayoutManager

        taskRecordService = TaskRecordService(activity!!)
        taskCountAdapter = TaskCountAdapter(activity!!)
        rv_count_list.adapter = taskCountAdapter

        taskCountAdapter.setOnItemClick(object : TaskCountAdapter.OnItemClick {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@CountFragment.context, TaskDetailsActivity::class.java)
                intent.putExtra("id", taskCountAdapter.mData!![position].id)
                startActivity(intent)
            }
        })

        taskCountAdapter.setOnItemLongClick(object : TaskCountAdapter.OnItemLongClick {
            override fun onItemLongClick(position: Int) {
                showDelDialog(position)
            }
        })

        updateData()
        return contentView
    }

    private fun updateData() {
        tv_loading.visibility = View.VISIBLE
        object : Thread() {
            override fun run() {
                val all = taskRecordService.queryMarkByStatus("3")
                taskCountAdapter.mData = all
                this@CountFragment.activity?.runOnUiThread {
                    taskCountAdapter.notifyDataSetChanged()
                    tv_loading.visibility = View.GONE
                }
            }
        }.start()
    }

    private fun showDelDialog(position: Int) {
        val recordTask = taskCountAdapter.mData!![position]
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setTitle("确认删除？")
        dialog.setIcon(R.mipmap.ic_launcher_round)
        dialog.setPositiveButton("确认") { _, _ ->
            recordTask.isDel = 1
            taskRecordService.delete(recordTask)
            taskCountAdapter.removeItem(position)
        }
        dialog.setNegativeButton("取消", null)
        dialog.create().show()
    }

    private inner class MyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "task_record") {
                if (intent.getIntExtra("type", 0) == 2) {
                    // 修改任务分类后 通知更新界面
                    val taskRecord = intent.getParcelableExtra<TaskRecord>("taskRecord")
                    if (taskRecord != null) {
                        taskCountAdapter.updateItem(taskRecord)
                    }
                }
            }
        }
    }
}