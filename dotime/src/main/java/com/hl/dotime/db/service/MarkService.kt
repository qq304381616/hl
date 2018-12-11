package com.hl.dotime.db.service

import android.content.ContentValues
import android.content.Context
import com.hl.dotime.db.MySQLiteOpenHelper
import com.hl.dotime.db.entity.Mark

class MarkService(private val mContext: Context) {

    companion object {
        private val TAG = MarkService.javaClass.simpleName
        private val TABLE_NAME = MySQLiteOpenHelper.MARK
    }

    fun insert(mark: Mark): Long {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.writableDatabase
        val value = ContentValues()
        value.put("name", mark.name)
        val id = db.insert(TABLE_NAME, null, value)
        db.close()
        return id
    }

    fun update(mark: Mark) {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.writableDatabase
        val value = ContentValues()
        value.put("name", mark.name)
        db.update(TABLE_NAME, value, "id=?", arrayOf(mark.id.toString()))
        db.close()
    }

    fun queryById(paramsId: Int): Mark? {
        val helper = MySQLiteOpenHelper(mContext)
        val db = helper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "id=?", arrayOf(paramsId.toString()), null, null, null)
        var t: Mark? = null
        while (cursor.moveToNext()) {
            t = Mark()
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            t.id = id
            t.name = name
        }
        if (cursor != null) cursor.close()
        db.close()
        return t;
    }
}