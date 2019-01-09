package com.hl.greendao.core.database;

import android.database.Cursor;
import android.database.SQLException;

public interface Database {

    void execSQL(String sql) throws SQLException;

    void beginTransaction();

    void endTransaction();

    void setTransactionSuccessful();

    Object getRawDatabase();

    boolean isDbLockedByCurrentThread();

    DatabaseStatement compileStatement(String sql);

    Cursor rawQuery(String sql, String[] parmeters);
}
