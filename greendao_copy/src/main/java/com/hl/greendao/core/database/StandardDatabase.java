package com.hl.greendao.core.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StandardDatabase implements Database {
    private final SQLiteDatabase delegate;

    public StandardDatabase(SQLiteDatabase delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execSQL(String sql) throws SQLException {
        delegate.execSQL(sql);
    }

    @Override
    public Object getRawDatabase() {
        return delegate;
    }
}
