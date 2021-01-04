package com.hl.dotime.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.hl.dotime.R
import com.hl.dotime.base.BaseActivity
import com.hl.dotime.db.entity.RecordTimer
import com.hl.dotime.db.service.RecordTimerService
import com.hl.dotime.utils.DateUtils
import com.hl.dotime.utils.Utils
import java.util.*

/**
 * 时间管理
 */
class TimerManagerActivity : BaseActivity() {

    private lateinit var tv_start_date: TextView
    private lateinit var tv_start_time: TextView
    private lateinit var tv_end_date: TextView
    private lateinit var tv_end_time: TextView
    private lateinit var tv_duration: TextView
    private lateinit var btn_save: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_manager)

        initToolbar()

        tv_start_date = findViewById(R.id.tv_start_date)
        tv_start_time = findViewById(R.id.tv_start_time)
        tv_end_date = findViewById(R.id.tv_end_date)
        tv_end_time = findViewById(R.id.tv_end_time)
        tv_duration = findViewById(R.id.tv_duration)
        btn_save = findViewById(R.id.btn_save)

        val recordTimerService = RecordTimerService(this)
        val timer = intent.getParcelableExtra<RecordTimer>("timer")

        val start = Calendar.getInstance()
        start.timeInMillis = timer!!.startTime!!
        var sY = start.get(Calendar.YEAR)
        var sM = start.get(Calendar.MONTH) + 1
        var sD = start.get(Calendar.DAY_OF_MONTH)
        var sH = start.get(Calendar.HOUR_OF_DAY)
        var sF = start.get(Calendar.MINUTE)
        var sS = start.get(Calendar.SECOND)

        val end = Calendar.getInstance()
        end.timeInMillis = timer.endTime!!
        var eY = end.get(Calendar.YEAR)
        var eM = end.get(Calendar.MONTH) + 1
        var eD = end.get(Calendar.DAY_OF_MONTH)
        var eH = end.get(Calendar.HOUR_OF_DAY)
        var eF = end.get(Calendar.MINUTE)
        var eS = end.get(Calendar.SECOND)

        tv_start_date.text = Utils.format(sY) + "-" + Utils.format(sM) + "-" + Utils.format(sD)
        tv_start_time.text = Utils.format(sH) + ":" + Utils.format(sF) + ":" + Utils.format(sS)
        tv_end_date.text = Utils.format(eY) + "-" + Utils.format(eM) + "-" + Utils.format(eD)
        tv_end_time.text = Utils.format(eH) + ":" + Utils.format(eF) + ":" + Utils.format(eS)

        if (timer.endTime == null || timer.endTime == 0L) {
            tv_end_date.visibility = View.INVISIBLE
            tv_end_time.visibility = View.INVISIBLE
            tv_duration.visibility = View.INVISIBLE
        }

        tv_duration.text = "持续时间: " + DateUtils.getTime(timer.endTime!! - timer.startTime!!)

        btn_save.setOnClickListener {
            if (timer.endTime == null || timer.endTime == 0L) {
                if (timer.startTime!! <= System.currentTimeMillis()) {
                    recordTimerService.updateStartTime(timer)
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "开始时间不能晚于当前时间", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }
            if (timer.endTime!! < timer.startTime!!) {
                Toast.makeText(this, "结束时间不能早于开始时间", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            recordTimerService.updateStartTime(timer)
            recordTimerService.updateEndTime(timer)
            setResult(Activity.RESULT_OK)
            finish()
        }

        tv_start_date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@TimerManagerActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                sY = year
                sM = month + 1
                sD = dayOfMonth
                val c = Calendar.getInstance()
                c.set(sY, sM - 1, sD, sH, sF, sS)
                timer.startTime = c.timeInMillis / 1000 * 1000
                tv_start_date.text = Utils.format(sY) + "-" + Utils.format(sM) + "-" + Utils.format(sD)
                tv_duration.text = "持续时间: " + DateUtils.getTime(timer.endTime!! - timer.startTime!!)
            }, sY, sM - 1, sD)
            datePickerDialog.show()
        }
        tv_start_time.setOnClickListener {
            val dialog = TimePickerDialog(this@TimerManagerActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                sH = hourOfDay
                sF = minute
                val c = Calendar.getInstance()
                c.set(sY, sM - 1, sD, sH, sF, sS)
                timer.startTime = c.timeInMillis / 1000 * 1000
                tv_start_time.text = Utils.format(sH) + ":" + Utils.format(sF) + ":" + Utils.format(sS)
                tv_duration.text = "持续时间: " + DateUtils.getTime(timer.endTime!! - timer.startTime!!)
            }, sH, sF, true)
            dialog.show()
        }
        tv_end_date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@TimerManagerActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                eY = year
                eM = month + 1
                eD = dayOfMonth
                val e = Calendar.getInstance()
                e.set(eY, eM - 1, eD, eH, eF, eS)
                timer.endTime = e.timeInMillis
                tv_end_date.text = Utils.format(eY) + "-" + Utils.format(eM) + "-" + Utils.format(eD)
                tv_duration.text = "持续时间: " + DateUtils.getTime(timer.endTime!! - timer.startTime!!)
            }, eY, eM - 1, eD)
            datePickerDialog.show()
        }
        tv_end_time.setOnClickListener {
            val dialog = TimePickerDialog(this@TimerManagerActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                eH = hourOfDay
                eF = minute
                val e = Calendar.getInstance()
                e.set(eY, eM - 1, eD, eH, eF, eS)
                timer.endTime = e.timeInMillis
                tv_end_time.text = Utils.format(eH) + ":" + Utils.format(eF) + ":" + Utils.format(eS)
                tv_duration.text = "持续时间: " + DateUtils.getTime(timer.endTime!! - timer.startTime!!)
            }, eH, eF, true)
            dialog.show()
        }
    }
}