package com.hl.dotime.java.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HL on 2018/5/19.
 */

public class MysqliteOpenHelperJava extends SQLiteOpenHelper {

    //数据库版本号
    private static Integer Version = 1;
    private static String dbName = "do_time";

    //在SQLiteOpenHelper的子类当中，必须有该构造函数
    public MysqliteOpenHelperJava(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MysqliteOpenHelperJava(Context context, String name, int version) {
        this(context, name, null, version);
    }


    public MysqliteOpenHelperJava(Context context, String name) {
        this(context, name, Version);
    }

    public MysqliteOpenHelperJava(Context context) {
        this(context, dbName);
    }

    //当数据库创建的时候被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("创建数据库和表");
        //创建了数据库并创建一个叫records的表
        //SQLite数据创建支持的数据类型： 整型数据，字符串类型，日期类型，二进制的数据类型
        String sql = "create table " + dbName + "(id int primary key,name varchar(200))";
        //execSQL用于执行SQL语句
        //完成数据库的创建
        db.execSQL(sql);
        //数据库实际上是没有被创建或者打开的，直到getWritableDatabase() 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
    }

    //数据库升级时调用
    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade（）方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("更新数据库版本为:" + newVersion);
    }
}
