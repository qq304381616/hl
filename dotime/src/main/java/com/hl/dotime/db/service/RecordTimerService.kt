package com.hl.dotime.db.service

import android.content.ContentValues
import android.content.Context
import com.hl.dotime.db.MySQLiteOpenHelper
import com.hl.dotime.db.entity.RecordTimer
import com.hl.dotime.utils.Constant

/**
 * Created by HL on 2018/5/19.
 * 计时 时间管理表
 */
class RecordTimerService(private val mContext: Context) {

    companion object {
        private val TAG = TaskService.javaClass.simpleName
        private val TABLE_NAME = MySQLiteOpenHelper.RECORD_TIMER
    }

    fun insert(recordTimer: RecordTimer) {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.writableDatabase
        val value = ContentValues()
        value.put("task_record_id", recordTimer.taskRecordId)
        value.put("start_time", recordTimer.startTime)
        value.put("end_Time", recordTimer.endTime)
        value.put("is_del", recordTimer.isDel)
        db.insert(TABLE_NAME, null, value)
        db.close()
    }

    fun updateStartTime(recordTimer: RecordTimer) {
        val dbHelper = MySQLiteOpenHelper(mContext)
        val db = dbHelper.writableDatabase
        val value = ContentValues()
        value.put("start_time", recordTimer.startTime)
        db.update(TABLE_NAME, value, "id=?", arrayOf(recordTimer.id.toString()))
        db.close()
    }

    fun updateEndTime(recordTimer: RecordTimer) {
        val dbHelper = MySQLiteOpenHelper(mContext)
        val db = dbHelper.writableDatabase
        val value = ContentValues()
        value.put("end_time", recordTimer.endTime)
        db.update(TABLE_NAME, value, "id=?", arrayOf(recordTimer.id.toString()))
        db.close()
    }

    fun updateDel(recordTimer: RecordTimer) {
        val dbHelper = MySQLiteOpenHelper(mContext)
        val db = dbHelper.writableDatabase
        val value = ContentValues()
        value.put("is_del", recordTimer.isDel)
        db.update(TABLE_NAME, value, "id=?", arrayOf(recordTimer.id.toString()))
        db.close()
    }

    fun queryByTaskRecordId(pTaskRecordId: String): ArrayList<RecordTimer> {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "task_record_id=? and (is_del is null or is_del = ?)", arrayOf(pTaskRecordId, Constant.FLAG_UN_DEL.toString()), null, null, null)
        val list = ArrayList<RecordTimer>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val taskRecordId = cursor.getString(cursor.getColumnIndex("task_record_id"))
            val startTime = cursor.getLong(cursor.getColumnIndex("start_time"))
            val endTime = cursor.getLong(cursor.getColumnIndex("end_time"))
            val t = RecordTimer();
            t.id = id
            t.taskRecordId = taskRecordId
            t.startTime = startTime
            t.endTime = endTime
            list.add(t)
        }
        if (cursor != null) cursor.close()
        db.close()
        return list;
    }
}