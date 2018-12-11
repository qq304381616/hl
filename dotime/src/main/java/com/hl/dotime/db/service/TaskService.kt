package com.hl.dotime.db.service

import android.content.ContentValues
import android.content.Context
import com.hl.dotime.db.MySQLiteOpenHelper
import com.hl.dotime.db.entity.Task
import java.util.*

/**
 * Created by HL on 2018/5/19.
 */
class TaskService(private val mContext: Context) {

    companion object {
        private val TAG = TaskService.javaClass.simpleName
        private val TABLE_NAME = MySQLiteOpenHelper.TASK
    }

    fun insert(task: Task) {
        // 创建SQLiteOpenHelper子类对象
        val helper = MySQLiteOpenHelper(mContext)
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        val db = helper.writableDatabase
        // 创建ContentValues对象
        val value = ContentValues()
        // 向该对象中插入键值对
        value.put("id", task.id)
        value.put("name", task.name)
        value.put("group_id", task.groupId)
        value.put("is_del", task.isDel)
        value.put("icon_name", task.iconName)
        value.put("icon_color", task.iconColor)
        // 调用insert()方法将数据插入到数据库当中
        db.insert(TABLE_NAME, null, value)
        // sqliteDatabase.execSQL("insert into user (id,name) values (1,'carson')");
        //关闭数据库
        db.close()
    }

    fun update(task: Task) {
        val dbHelper = MySQLiteOpenHelper(mContext)
        val db = dbHelper.writableDatabase
        val value = ContentValues()
        value.put("name", task.name)
        value.put("group_id", task.groupId)
        value.put("is_del", task.isDel)
        value.put("icon_name", task.iconName)
        value.put("icon_color", task.iconColor)
        db.update(TABLE_NAME, value, "id=?", arrayOf(task.id))
        db.close()
    }

    fun queryById(paramsId: String): Task {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "id=?", arrayOf(paramsId), null, null, null)
        val task = Task();
        if (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val groupId = cursor.getString(cursor.getColumnIndex("group_id"))
            val isDel = cursor.getInt(cursor.getColumnIndex("is_del"))
            val iconName = cursor.getString(cursor.getColumnIndex("icon_name"))
            val iconColor = cursor.getString(cursor.getColumnIndex("icon_color"))
            task.id = id
            task.name = name
            task.groupId = groupId
            task.isDel = isDel
            task.iconName = iconName
            task.iconColor = iconColor
        }
        if (cursor != null) cursor.close()
        db.close()
        return task
    }

    fun queryAll(): ArrayList<Task> {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "is_del=?", arrayOf("0"), null, null, null)
        val list: ArrayList<Task> = ArrayList()
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val groupId = cursor.getString(cursor.getColumnIndex("group_id"))
            val isDel = cursor.getInt(cursor.getColumnIndex("is_del"))
            val iconName = cursor.getString(cursor.getColumnIndex("icon_name"))
            val iconColor = cursor.getString(cursor.getColumnIndex("icon_color"))
            val task = Task();
            task.id = id
            task.name = name
            task.groupId = groupId
            task.isDel = isDel
            task.iconName = iconName
            task.iconColor = iconColor
            list.add(task)
        }
        if (cursor != null) cursor.close()
        db.close()
        return list
    }

    fun queryAllAndGroup(): ArrayList<Task> {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select t1.*,t2.name group_name from task t1 left join task_group t2 on t1.group_id = t2.id where t1.is_del = 0", arrayOf())
        val list: ArrayList<Task> = ArrayList()
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val groupId = cursor.getString(cursor.getColumnIndex("group_id"))
            val isDel = cursor.getInt(cursor.getColumnIndex("is_del"))
            val groupName = cursor.getString(cursor.getColumnIndex("group_name"))
            val iconName = cursor.getString(cursor.getColumnIndex("icon_name"))
            val iconColor = cursor.getString(cursor.getColumnIndex("icon_color"))
            val task = Task();
            task.id = id
            task.name = name
            task.groupId = groupId
            task.isDel = isDel
            task.groupName = groupName
            task.iconName = iconName
            task.iconColor = iconColor
            list.add(task)
        }
        if (cursor != null) cursor.close()
        db.close()
        return list;
    }
}