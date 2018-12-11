package com.hl.dotime.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hl.dotime.R
import com.hl.dotime.TaskManagerFragment
import com.hl.dotime.base.BaseActivity
import com.hl.dotime.db.entity.Task

class TaskManagerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)

        initToolbar()

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val taskManagerFragment = TaskManagerFragment()
        transaction.replace(R.id.container, taskManagerFragment)
        transaction.commit()
    }

    fun chooseCallback(task: Task) {
        val intent = Intent()
        intent.putExtra("task", task)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}