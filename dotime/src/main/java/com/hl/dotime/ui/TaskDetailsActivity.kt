package com.hl.dotime.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.*
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import com.hl.dotime.R
import com.hl.dotime.adapter.TimerAdapter
import com.hl.dotime.base.BaseActivity
import com.hl.dotime.db.entity.Mark
import com.hl.dotime.db.entity.RecordTimer
import com.hl.dotime.db.entity.Task
import com.hl.dotime.db.entity.TaskRecord
import com.hl.dotime.db.service.MarkService
import com.hl.dotime.db.service.RecordTimerService
import com.hl.dotime.db.service.TaskRecordService
import com.hl.dotime.db.service.TaskService
import com.hl.dotime.utils.ConstantIcon
import com.hl.dotime.utils.DateUtils
import com.hl.dotime.utils.UUIDUtils
import com.hl.dotime.utils.Utils

class TaskDetailsActivity : BaseActivity() {

    private var task: Task? = null
    private lateinit var toolbar: Toolbar
    private var mark: Mark? = null
    private lateinit var taskRecord: TaskRecord
    private lateinit var tv_task: TextView
    private lateinit var iv_icon: ImageView
    private lateinit var recordTimerService: RecordTimerService
    private lateinit var markService: MarkService
    private lateinit var taskService: TaskService
    private lateinit var taskRecordService: TaskRecordService
    private lateinit var timerAdapter: TimerAdapter
    private var updateTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true) //设置返回键可用
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP

        val tv_state = findViewById<TextView>(R.id.tv_state)
        tv_task = findViewById(R.id.tv_task)
        iv_icon = findViewById(R.id.iv_icon)
        val rv_timer_list = findViewById<RecyclerView>(R.id.rv_timer_list)
        val et_mark = findViewById<EditText>(R.id.et_mark)
        val btn_save = findViewById<Button>(R.id.btn_save)

        val mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_timer_list.setItemAnimator(DefaultItemAnimator())
        rv_timer_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_timer_list.setLayoutManager(mLinearLayoutManager)

        recordTimerService = RecordTimerService(this)
        markService = MarkService(this)
        taskRecordService = TaskRecordService(this)
        taskService = TaskService(this)

        findViewById<View>(R.id.rl_task).setOnClickListener {
            startActivityForResult(Intent(this, TaskManagerActivity::class.java), 1001)
        }

        val id = intent.getStringExtra("id")

        if (id != null) {
            taskRecord = taskRecordService.queryById(id)
            val allTimer = recordTimerService.queryByTaskRecordId(taskRecord.id!!)
            when (taskRecord.status) {
                1 -> {
                    tv_state.text = "任务进行中"
                }
                2 -> {
                    tv_state.text = "任务已暂停"
                }
                3 -> {
                    tv_state.text = "任务已结束"
                }
            }

            tv_task.text = taskRecord.name
            task = taskService.queryById(taskRecord.taskId!!)

            Utils.setSVGColor(this, iv_icon, ConstantIcon.map[task!!.iconName]!!, task!!.iconColor!!)

            timerAdapter = TimerAdapter(this)
            timerAdapter.setOnItemClick(object : TimerAdapter.OnItemClick {
                override fun onItemClick(position: Int) {
                    val intent = Intent(this@TaskDetailsActivity, TimerManagerActivity::class.java)
                    intent.putExtra("timer", timerAdapter.mData!!.get(position))
                    startActivityForResult(intent, 1002)
                }
            })
            timerAdapter.setOnItemLongClick(object : TimerAdapter.OnItemLongClick {
                override fun onItemLongClick(position: Int) {
                    showDelDialog(position)
                }
            })

            timerAdapter.mData = allTimer
            rv_timer_list.adapter = timerAdapter

            if (taskRecord.markId != null && taskRecord.markId != 0) {
                mark = markService.queryById(taskRecord.markId!!)
                et_mark.setText(mark!!.name)
            }
        } else {
            taskRecord = TaskRecord()
            tv_state.visibility = View.GONE
        }

        btn_save.setOnClickListener {
            if (taskRecord.taskId != null) { // 修改
                // 如果修改任务，则保存
                if (task != null && !taskRecord.taskId.equals(task?.id)) {
                    taskRecord.taskId = task!!.id
                    taskRecord.name = task!!.name
                    taskRecordService.updateTask(taskRecord)
                }
            } else { // 增加
                if (task != null) {
                    taskRecord.id = UUIDUtils.uuid
                    taskRecord.taskId = task!!.id
                    taskRecord.name = task!!.name
                    taskRecord.status = 1
                    taskRecord.isDel = 0
                    taskRecordService.insert(taskRecord)
                    val timer = RecordTimer()
                    timer.taskRecordId = taskRecord.id
                    timer.startTime = DateUtils.getSeconds()
                    recordTimerService.insert(timer)

                    val timerList = recordTimerService.queryByTaskRecordId(taskRecord.id!!)
                    taskRecord.timerList = timerList
                } else {
                    Toast.makeText(this, "请选择任务", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            //　存储描述
            if (taskRecord.markId != null && taskRecord.markId != 0) {
                mark!!.name = et_mark.text.toString()
                markService.update(mark!!)
            } else {
                mark = Mark()
                mark!!.name = et_mark.text.toString()
                val markId = markService.insert(mark!!)
                mark!!.id = markId.toInt()
                taskRecord.markId = markId.toInt()
                taskRecordService.updateMark(taskRecord)
            }
            taskRecord.mark = mark
            taskRecord.task = taskService.queryById(taskRecord.taskId!!)

            // 通知更新列表界面
            val intent = Intent()
            intent.action = "task_record"
            intent.putExtra("type",
                    if (id == null) {
                        1
                    } else {
                        2
                    })
            intent.putExtra("taskRecord", taskRecord)
            sendBroadcast(intent)

            finish()
        }
    }

    private fun showDelDialog(position: Int) {
        val timer = timerAdapter.mData!!.get(position)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("确认删除？")
        builder.setIcon(R.mipmap.ic_launcher_round)
        builder.setPositiveButton("确认") { _, _ ->
            updateTime = true;
            timer.isDel = 1
            recordTimerService.updateDel(timer)
            timerAdapter.removeItem(position)

            if ((timer.endTime == null || timer.endTime == 0L) && taskRecord.status == 1) {
                taskRecord.status = 2;
                taskRecordService.update(taskRecord)
            }
        }
        builder.create().show()
    }

    override fun onBack() {
        super.onBack()
        if (updateTime) { // 返回时 判断时间是否需要刷新
            taskRecord.task = taskService.queryById(taskRecord.taskId!!)
            val intent = Intent()
            intent.action = "task_record"
            intent.putExtra("type", 2)
            intent.putExtra("taskRecord", taskRecord)
            sendBroadcast(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1001 -> {
                if (resultCode == Activity.RESULT_OK) {
                    task = data!!.getParcelableExtra<Task>("task")
                    tv_task.text = task?.name
                    Utils.setSVGColor(this, iv_icon, ConstantIcon.map[task!!.iconName]!!, task!!.iconColor!!)
                }
            }
            1002 -> {
                if (resultCode == Activity.RESULT_OK) {
                    updateTime = true
                    val allTimer = recordTimerService.queryByTaskRecordId(taskRecord.id!!)
                    taskRecord.timerList = allTimer
                    timerAdapter.mData = allTimer
                    timerAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}