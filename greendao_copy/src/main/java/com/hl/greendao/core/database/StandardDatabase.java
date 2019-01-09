package com.hl.greendao.core.database;

import android.database.Cursor;
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
    public void beginTransaction() {
        delegate.beginTransaction();
    }

    @Override
    public void endTransaction() {
        delegate.endTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        delegate.setTransactionSuccessful();
    }

    @Override
    public Object getRawDatabase() {
        return delegate;
    }

    @Override
    public boolean isDbLockedByCurrentThread() {
        return delegate.isDbLockedByCurrentThread();
    }

    @Override
    public DatabaseStatement compileStatement(String sql) {
        return new StandardDatabaseStatement(delegate.compileStatement(sql));
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return delegate.rawQuery(sql, selectionArgs);
    }

}
