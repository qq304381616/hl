package com.hl.dotime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hl.dotime.db.entity.Task
import com.hl.dotime.db.service.TaskService
import com.hl.dotime.ui.ModifyTaskActivity
import com.hl.dotime.ui.TaskManagerActivity


/**
 * Created by HL on 2018/5/16.
 * 任务分类界面
 */
class TaskManagerFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskService: TaskService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_task_manager, null)

        val rv_parent = contentView.findViewById<RecyclerView>(R.id.rv_parent)
        val fab = contentView.findViewById<FloatingActionButton>(R.id.fab)

        taskService = TaskService(activity!!)
        val queryAll = taskService.queryAllAndGroup()

        taskAdapter = TaskAdapter(activity!!)

        // 点击修改
        taskAdapter.setOnItemClick(object : TaskAdapter.OnItemClick {
            override fun onItemClick(position: Int) {
                val t = taskAdapter.mData!![position]
                if (activity is TaskManagerActivity) {
                    (activity as TaskManagerActivity).chooseCallback(t)
                } else {
                    val intent = Intent(activity, ModifyTaskActivity::class.java)
                    intent.putExtra("task", t)
                    startActivityForResult(intent, 1001)
                }
            }
        })

        // 长按删除
        taskAdapter.setOnItemLongClick(object : TaskAdapter.OnItemLongClick {
            override fun onItemLongClick(position: Int) {
                showDelDialog(position)
            }
        })

        taskAdapter.mData = queryAll
        rv_parent.itemAnimator = DefaultItemAnimator()
        rv_parent.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_parent.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_parent.adapter = taskAdapter

        fab.setOnClickListener {
            startActivityForResult(Intent(activity, ModifyTaskActivity::class.java), 1002)
        }
        return contentView
    }

    private fun showDelDialog(position: Int) {
        val task = taskAdapter.mData!![position]
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setTitle("确认删除？")
        dialog.setIcon(R.mipmap.ic_launcher_round)
        dialog.setPositiveButton("确认") { _, _ ->
            task.isDel = 1
            task.updateTime = System.currentTimeMillis()
            taskService.update(task)
            taskAdapter.removeItem(position)

            val intent = Intent()
            intent.action = "task"
            intent.putExtra("type", 3)
            intent.putExtra("task", task)
            activity!!.sendBroadcast(intent)
        }
        dialog.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1001 -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 更新列表中 修改的那条记录
                    val task = data!!.getParcelableExtra<Task>("task")
                    for (i in taskAdapter.mData!!.indices) {
                        if (task.id == taskAdapter.mData!!.get(i).id) {
                            taskAdapter.mData!!.set(i, task)
                            taskAdapter.notifyItemChanged(i)

                            val intent = Intent()
                            intent.action = "task"
                            intent.putExtra("type", 2)
                            intent.putExtra("task", task)
                            activity!!.sendBroadcast(intent)
                            break
                        }
                    }
                }
            }
            1002 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val task = data!!.getParcelableExtra<Task>("task")
                    taskAdapter.insertItem(taskAdapter.mData!!.size, task)
                    val intent = Intent()
                    intent.action = "task"
                    intent.putExtra("type", 1)
                    intent.putExtra("task", task)
                    activity!!.sendBroadcast(intent)
                }
            }
        }
    }

    companion object {
        private val EXTRA_CONTENT = "content"
        private val TAG = "TaskManagerFragment"

        fun newInstance(content: String): TaskManagerFragment {
            val arguments = Bundle()
            arguments.putString(EXTRA_CONTENT, content)
            val taskManagerFragment = TaskManagerFragment()
            taskManagerFragment.arguments = arguments
            return taskManagerFragment
        }
    }
}