package com.hl.dotime.db.service

import android.content.ContentValues
import android.content.Context
import com.hl.dotime.db.MySQLiteOpenHelper
import com.hl.dotime.db.entity.TaskGroup
import java.util.*

/**
 * Created by HL on 2018/5/19.
 */
class TaskGroupService(private val mContext: Context) {

    companion object {
        private const val TABLE_NAME = MySQLiteOpenHelper.TASK_GROUP
    }

    fun insert(taskGroup: TaskGroup) {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.writableDatabase
        val value = ContentValues()
        value.put("id", taskGroup.id)
        value.put("parent_id", taskGroup.parentId)
        value.put("name", taskGroup.name)
        value.put("icon", taskGroup.icon)
        value.put("is_del", taskGroup.isDel)
        value.put("create_time", taskGroup.createTime)
        value.put("update_time", taskGroup.updateTime)
        db.insert(TABLE_NAME, null, value)
        db.close()
    }

    fun updateName(taskGroup: TaskGroup) {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.writableDatabase
        val value = ContentValues()
        value.put("name", taskGroup.name)
        value.put("update_time", taskGroup.updateTime)
        db.update(TABLE_NAME, value, "id=?", arrayOf(taskGroup.id))
        db.close()
    }

    fun updateDel(taskGroup: TaskGroup) {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.writableDatabase
        val value = ContentValues()
        value.put("is_del", taskGroup.isDel)
        value.put("update_time", taskGroup.updateTime)
        db.update(TABLE_NAME, value, "id=?", arrayOf(taskGroup.id))
        db.close()
    }

    fun queryAll(): ArrayList<TaskGroup> {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "is_del=?", arrayOf("0"), null, null, null)
        val list: ArrayList<TaskGroup> = ArrayList()
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("id"))
            val parentId = cursor.getString(cursor.getColumnIndex("parent_id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val icon = cursor.getString(cursor.getColumnIndex("icon"))
            val isDel = cursor.getInt(cursor.getColumnIndex("is_del"))
            val createTime = cursor.getLong(cursor.getColumnIndex("create_time"))
            val updateTime = cursor.getLong(cursor.getColumnIndex("update_time"))
            val group = TaskGroup()
            group.id = id
            group.parentId = parentId
            group.name = name
            group.icon = icon
            group.isDel = isDel
            group.createTime = createTime
            group.updateTime = updateTime
            list.add(group)
        }
        cursor?.close()
        db.close()
        return list;
    }
}