package com.hl.dotime.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

class MySQLiteOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int)//必须通过super调用父类当中的构造函数
    : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val TAG = "MySQLiteOpenHelper"

        //数据库版本号
        const val Version = 3
        const val dbName = "do_time"
        const val TASK_GROUP = "task_group"  // 任务组表，多级
        const val TASK = "task" // 任务表
        const val TASK_RECORD = "task_record" // 任务计时表
        const val RECORD_TIMER = "record_timer" // 任务计时表
        const val MARK = "mark" // 说明表
    }

    //参数说明
    //context:上下文对象
    //name:数据库名称
    //param:factory
    //version:当前数据库的版本，值必须是整数并且是递增的状态
    @JvmOverloads
    constructor(context: Context, name: String = dbName, version: Int = Version) : this(context, name, null, version)

    //当数据库创建的时候被调用
    override fun onCreate(db: SQLiteDatabase) {
        println("创建数据库和表")
        //创建了数据库和表
        //SQLite数据创建支持的数据类型： 整型数据，字符串类型，日期类型，二进制的数据类型
        //execSQL用于执行SQL语句
        //完成数据库的创建
        //数据库实际上是没有被创建或者打开的，直到getWritableDatabase() 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
        db.execSQL("create table $TASK_GROUP (id varchar(32) primary key, parent_id varchar(32) ,name varchar(200), icon varchar(200), mark_id Integer, is_del Integer)")
        db.execSQL("create table $TASK (id varchar(200) primary key, group_id varchar(200) ,name varchar(200), is_del Integer)")
        db.execSQL("create table $TASK_RECORD (id varchar(32) primary key,task_id INTEGER, name varchar(200), status INTEGER, mark_id Integer, is_del Integer)")
        db.execSQL("create table $RECORD_TIMER (id Integer primary key AUTOINCREMENT, task_record_id varchar(32), start_time Long, end_time Long, is_del Integer)")
        db.execSQL("create table $MARK (id Integer primary key AUTOINCREMENT, name varchar(200))")

        updateTo2(db)
        updateTo3(db)
    }

    //数据库升级时调用
    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade（）方法
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(TAG, "旧数据库版本：$oldVersion")
        Log.e(TAG, "新数据库版本：$newVersion")
        when (oldVersion) {
            1 -> {
                updateTo2(db)
                update2Date(db)
            }
            2 -> {
                updateTo3(db)
                update3Date(db)
            }
        }
    }

    // 增加列
    private fun updateTo2(db: SQLiteDatabase) {
        db.execSQL("ALTER TABLE $TASK ADD icon_name text")
        db.execSQL("ALTER TABLE $TASK ADD icon_color text")
    }

    // 增加默认数据
    private fun update2Date(db: SQLiteDatabase) {
        db.execSQL("UPDATE $TASK SET icon_name = '" + Date() + "'")
    }

    // 增加列
    private fun updateTo3(db: SQLiteDatabase) {
        db.execSQL("ALTER TABLE $TASK ADD create_time Long")
        db.execSQL("ALTER TABLE $TASK_GROUP ADD create_time Long")
    }

    // 增加默认数据
    private fun update3Date(db: SQLiteDatabase) {
        db.execSQL("UPDATE $TASK SET create_time = '" + System.currentTimeMillis() + "'")
        db.execSQL("UPDATE $TASK_GROUP SET create_time = '" + System.currentTimeMillis() + "'")
    }
}
