package com.hl.dotime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hl.dotime.db.entity.RecordTimer
import com.hl.dotime.db.entity.Task
import com.hl.dotime.db.entity.TaskRecord
import com.hl.dotime.db.service.RecordTimerService
import com.hl.dotime.db.service.TaskRecordService
import com.hl.dotime.db.service.TaskService
import com.hl.dotime.ui.TaskDetailsActivity
import com.hl.dotime.utils.DateUtils
import com.hl.dotime.utils.UUIDUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by HL on 2018/5/16.
 * 计时界面
 */
class TaskFragment : Fragment() {

    private lateinit var taskStaringAdapter: TaskStaringAdapter
    private lateinit var taskQuickAdapter: TaskQuickAdapter
    private lateinit var receiver: MyReceiver
    private lateinit var intentFilter: IntentFilter
    private lateinit var taskRecordService: TaskRecordService
    private lateinit var recordTimerService: RecordTimerService
    private lateinit var taskService: TaskService
    private lateinit var rv_task_list: RecyclerView
    private var subscribe: Disposable? = null

    companion object {
        private val EXTRA_CONTENT = "content"

        fun newInstance(content: String): TaskFragment {
            val arguments = Bundle()
            arguments.putString(EXTRA_CONTENT, content)
            val tabContentFragment = TaskFragment()
            tabContentFragment.arguments = arguments
            return tabContentFragment
        }
    }

    private inner class MyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "task") {
                val task = intent.getParcelableExtra<Task>("task")
                if (intent.getIntExtra("type", 0) == 1) {
                    taskQuickAdapter.insertItem(taskQuickAdapter.mData!!.size, task)
                } else if (intent.getIntExtra("type", 0) == 2) {
                    for (i in taskQuickAdapter.mData!!.indices) {
                        if (task.id == taskQuickAdapter.mData!!.get(i).id) {
                            taskQuickAdapter.mData!!.set(i, task)
                            taskQuickAdapter.notifyItemChanged(i)
                            break
                        }
                    }
                    for (i in taskStaringAdapter.mData!!.indices) {
                        if (task.id == taskStaringAdapter.mData!!.get(i).taskId) {
                            taskStaringAdapter.mData!!.get(i).task = task
                            taskStaringAdapter.notifyItemChanged(i)
                            break
                        }
                    }
                } else if (intent.getIntExtra("type", 0) == 3) {
                    for (i in taskQuickAdapter.mData!!.indices) {
                        if (task.id == taskQuickAdapter.mData!!.get(i).id) {
                            taskQuickAdapter.mData!!.set(i, task)
                            taskQuickAdapter.removeItem(i)
                            break
                        }
                    }
                }
            } else if (intent.action == "task_record") {
                if (intent.getIntExtra("type", 0) == 1) {
                    // 修改任务分类后 通知更新界面
                    val taskRecord = intent.getParcelableExtra<TaskRecord>("taskRecord")
                    if (taskRecord != null) {
                        taskStaringAdapter.insertItem(0, taskRecord)
                        rv_task_list.scrollToPosition(0) // 滑动到每1个位置
                    }
                } else if (intent.getIntExtra("type", 0) == 2) {
                    // 修改任务分类后 通知更新界面
                    val taskRecord = intent.getParcelableExtra<TaskRecord>("taskRecord")
                    if (taskRecord != null) {
                        taskRecord.timerList = recordTimerService.queryByTaskRecordId(taskRecord.id!!)
                        taskStaringAdapter.updateItem(taskRecord)
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_task, null)

        receiver = MyReceiver()
        intentFilter = IntentFilter()
        intentFilter.addAction("task")
        intentFilter.addAction("task_record")
        activity!!.registerReceiver(receiver, intentFilter)

        rv_task_list = contentView.findViewById(R.id.rv_task_list)

        rv_task_list.itemAnimator = DefaultItemAnimator()
        rv_task_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_task_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        taskRecordService = TaskRecordService(activity!!)
        recordTimerService = RecordTimerService(activity!!)
        val queryAll1 = taskRecordService.queryMarkByStatus("1", "2")
        taskStaringAdapter = TaskStaringAdapter(activity!!)
        taskStaringAdapter.setOnItemClick(object : TaskStaringAdapter.OnItemClick {
            override fun onStartOrStop(position: Int) {
                val task = taskStaringAdapter.mData!!.get(position)
                if (task.status == 1) {
                    val timerList = recordTimerService.queryByTaskRecordId(task.id!!)
                    for (i in timerList) {
                        if (i.endTime == null || i.endTime == 0L) {
                            i.endTime = DateUtils.getSeconds()
                            recordTimerService.updateEndTime(i)
                            break
                        }
                    }
                    task.status = 2
                } else {
                    val timer = RecordTimer()
                    timer.taskRecordId = task.id
                    timer.startTime = DateUtils.getSeconds()
                    recordTimerService.insert(timer)
                    task.status = 1
                }
                task.timerList = recordTimerService.queryByTaskRecordId(task.id!!)
                taskRecordService.update(task)
                taskStaringAdapter.notifyItemChanged(position)
            }

            override fun onStopClick(position: Int) {
                val task = taskStaringAdapter.mData!!.get(position)
                task.status = 3
                taskRecordService.update(task)
                val timerList = recordTimerService.queryByTaskRecordId(task.id!!)
                for (i in timerList) {
                    if (i.endTime == null || i.endTime == 0L) {
                        i.endTime = DateUtils.getSeconds()
                        recordTimerService.updateEndTime(i)
                        break
                    }
                }
            }

            // 查看详情
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, TaskDetailsActivity::class.java)
                intent.putExtra("id", taskStaringAdapter.mData!!.get(position).id)
                startActivity(intent)
            }
        })
        taskStaringAdapter.setOnItemLongClick(object : TaskStaringAdapter.OnItemLongClick {
            override fun onItemLongClick(position: Int) {
                showDelDialog(position)
            }
        })

        taskStaringAdapter.mData = queryAll1
        rv_task_list.adapter = taskStaringAdapter

        val rv_task = contentView.findViewById<RecyclerView>(R.id.rv_task)

        rv_task.itemAnimator = DefaultItemAnimator()
        rv_task.layoutManager = GridLayoutManager(activity, 4)

        taskQuickAdapter = TaskQuickAdapter(activity!!)
        taskService = TaskService(activity!!)
        taskQuickAdapter.mData = taskService.queryAll()
        taskQuickAdapter.setOnItemClick(object : TaskQuickAdapter.OnItemClick {
            override fun onItemClick(position: Int) {
                val t = taskQuickAdapter.mData!![position]
                val r = TaskRecord()
                r.id = UUIDUtils.uuid
                r.taskId = t.id
                r.name = t.name
                r.status = 1
                r.isDel = 0
                taskRecordService.insert(r)

                val timer = RecordTimer()
                timer.taskRecordId = r.id
                timer.startTime = DateUtils.getSeconds()
                recordTimerService.insert(timer)

                val timerList = recordTimerService.queryByTaskRecordId(r.id!!)
                r.timerList = timerList
                r.task = taskService.queryById(r.taskId!!)
                taskStaringAdapter.insertItem(0, r)

                t.lastUseTime = System.currentTimeMillis()
                taskService.updateLastUseTime(t)

                rv_task_list.scrollToPosition(0) // 滑动到每1个位置
            }
        })

        rv_task.adapter = taskQuickAdapter

        // 定时1秒 循环更新计时器
        subscribe = Observable.interval(1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { aLong ->
                    if (taskStaringAdapter.itemCount > 0) {
                        taskStaringAdapter.notifyDataSetChanged()
                    }
                }

        return contentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity!!.unregisterReceiver(receiver)
        if (subscribe != null) {
            subscribe!!.dispose()
        }
    }

    private fun showDelDialog(position: Int) {
        val recordTask = taskStaringAdapter.mData!!.get(position)
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setTitle("确认删除？")
        dialog.setIcon(R.mipmap.ic_launcher_round)
        dialog.setPositiveButton("确认") { _, _ ->
            recordTask.isDel = 1
            taskRecordService.delete(recordTask)
            taskStaringAdapter.removeItem(position)
        }
        dialog.setNegativeButton("取消", null)
        dialog.create().show()
    }
}