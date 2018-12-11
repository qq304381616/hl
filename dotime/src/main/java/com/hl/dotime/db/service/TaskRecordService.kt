package com.hl.dotime.db.service

import android.content.ContentValues
import android.content.Context
import com.hl.dotime.db.MySQLiteOpenHelper
import com.hl.dotime.db.entity.FilterList
import com.hl.dotime.db.entity.Mark
import com.hl.dotime.db.entity.TaskRecord
import java.util.*

/**
 * Created by HL on 2018/5/21.
 */
class TaskRecordService(private val mContext: Context) {

    companion object {
        private val TABLE_NAME = MySQLiteOpenHelper.TASK_RECORD
    }

    fun insert(taskRecord: TaskRecord) {
        // 创建SQLiteOpenHelper子类对象
        val dbHelper1 = MySQLiteOpenHelper(mContext)
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        val sqliteDatabase1 = dbHelper1.writableDatabase

        // 创建ContentValues对象
        val values1 = ContentValues()

        // 向该对象中插入键值对
        values1.put("id", taskRecord.id)
        values1.put("task_id", taskRecord.taskId)
        values1.put("name", taskRecord.name)
        values1.put("status", taskRecord.status)
        values1.put("mark_id", taskRecord.markId)
        values1.put("is_del", taskRecord.isDel)

        // 调用insert()方法将数据插入到数据库当中
        sqliteDatabase1.insert(TABLE_NAME, null, values1)
        // sqliteDatabase.execSQL("insert into user (id,name) values (1,'carson')");

        //关闭数据库
        sqliteDatabase1.close()
    }

    fun update(taskRecord: TaskRecord) {
        val dbHelper = MySQLiteOpenHelper(mContext)
        val db = dbHelper.writableDatabase
        val value = ContentValues()
        value.put("status", taskRecord.status)
        db.update(TABLE_NAME, value, "id=?", arrayOf(taskRecord.id))
        db.close()
    }

    fun delete(taskRecord: TaskRecord) {
        val dbHelper = MySQLiteOpenHelper(mContext)
        val db = dbHelper.writableDatabase
        val value = ContentValues()
        value.put("is_del", taskRecord.isDel)
        db.update(TABLE_NAME, value, "id=?", arrayOf(taskRecord.id))
        db.close()
    }

    fun updateMark(taskRecord: TaskRecord) {
        val dbHelper = MySQLiteOpenHelper(mContext)
        val db = dbHelper.writableDatabase
        val value = ContentValues()
        value.put("mark_id", taskRecord.markId)
        db.update(TABLE_NAME, value, "id=?", arrayOf(taskRecord.id))
        db.close()
    }

    fun updateTask(taskRecord: TaskRecord) {
        val dbHelper = MySQLiteOpenHelper(mContext)
        val db = dbHelper.writableDatabase
        val value = ContentValues()
        value.put("task_id", taskRecord.taskId)
        value.put("name", taskRecord.name)
        db.update(TABLE_NAME, value, "id=?", arrayOf(taskRecord.id))
        db.close()
    }

    fun queryMarkByStatus(vararg paramsStatus: String): ArrayList<TaskRecord> {
        val recordTimerService = RecordTimerService(this.mContext);
        val taskService = TaskService(this.mContext);
        val dbHelper4 = MySQLiteOpenHelper(mContext)
        val db = dbHelper4.readableDatabase
        var selection: String? = ""
        if (paramsStatus.size > 0) {
            // 组合参数where 条件
            selection = "and t1.status in ( "
            for (status in paramsStatus) {
                selection += "?,"
            }
            selection = selection.substring(0, selection.length - 1)
            selection += " )"
        }
        val cursor = db.rawQuery("select t1.rowid rowid, t1.*, t2.name mark from task_record t1 left join mark t2 on t1.mark_id = t2.id where t1.is_del = 0 " + selection + "order by rowid desc", paramsStatus)
        val list = ArrayList<TaskRecord>()
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("id"))
            val taskId = cursor.getString(cursor.getColumnIndex("task_id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val status = cursor.getInt(cursor.getColumnIndex("status"))
            val markId = cursor.getInt(cursor.getColumnIndex("mark_id"))
            val isDel = cursor.getInt(cursor.getColumnIndex("is_del"))
            val mark = cursor.getString(cursor.getColumnIndex("mark"))
            val taskP = TaskRecord();
            taskP.id = id
            taskP.taskId = taskId
            taskP.name = name
            taskP.status = status
            taskP.markId = markId
            taskP.isDel = isDel
            taskP.mark = Mark(mark)
            taskP.timerList = recordTimerService.queryByTaskRecordId(id)
            taskP.task = taskService.queryById(taskId)
            list.add(taskP)
        }
        if (cursor != null) cursor.close()
        db.close()
        return list;
    }

    // 根据开始时间和结束时间筛选数据
    fun queryByTime(start: String, end: String): ArrayList<FilterList> {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
                "select t1.id, t1.task_record_id, t1.start_time, t1.end_time, t2.task_id, t2.name, t2.status, t3.name mark, t4.icon_name, t4.icon_color " +
                        "from record_timer t1 " +
                        "left join task_record t2 on t1.task_record_id = t2.id " +
                        "left join mark t3 on t2.mark_id = t3.id " +
                        "left join task t4 on t2.task_id = t4.id " +
                        "where t2.is_del = 0 and (t1.is_del is null or t1.is_del = 0) and t2.status = 3 and t1.start_time >= ? and t1.end_time <= ? " +
                        "order by t1.start_time desc"
                , arrayOf(start, end))
        val list = ArrayList<FilterList>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val taskRecordId = cursor.getString(cursor.getColumnIndex("task_record_id"))
            val startTime = cursor.getLong(cursor.getColumnIndex("start_time"))
            val endTime = cursor.getLong(cursor.getColumnIndex("end_time"))
            val taskId = cursor.getString(cursor.getColumnIndex("task_id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val status = cursor.getInt(cursor.getColumnIndex("status"))
            val mark = cursor.getString(cursor.getColumnIndex("mark"))
            val iconName = cursor.getString(cursor.getColumnIndex("icon_name"))
            val iconColor = cursor.getString(cursor.getColumnIndex("icon_color"))
            val f = FilterList();
            f.id = id
            f.taskRecordId = taskRecordId
            f.startTime = startTime
            f.endTime = endTime
            f.taskId = taskId
            f.name = name
            f.status = status
            f.mark = mark
            f.iconName = iconName
            f.iconColor = iconColor
            list.add(f)
        }
        if (cursor != null) cursor.close()
        db.close()
        return list;
    }

    fun queryById(paramsId: String): TaskRecord {
        val recordTimerService = RecordTimerService(this.mContext);
        val dbHelper4 = MySQLiteOpenHelper(mContext)
        val db = dbHelper4.readableDatabase
        val cursor = db.rawQuery("select t1.*, t2.name mark  from task_record t1 left join mark t2 on t1.mark_id = t2.id where t1.id = ?", arrayOf(paramsId))
        val task = TaskRecord()
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("id"))
            val taskId = cursor.getString(cursor.getColumnIndex("task_id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val status = cursor.getInt(cursor.getColumnIndex("status"))
            val markId = cursor.getInt(cursor.getColumnIndex("mark_id"))
            val isDel = cursor.getInt(cursor.getColumnIndex("is_del"))
            val mark = cursor.getString(cursor.getColumnIndex("mark"))
            task.id = id
            task.taskId = taskId
            task.name = name
            task.status = status
            task.markId = markId
            task.isDel = isDel
            task.mark = Mark(mark)
            task.timerList = recordTimerService.queryByTaskRecordId(id)
        }
        if (cursor != null) cursor.close()
        db.close()
        return task;
    }
}