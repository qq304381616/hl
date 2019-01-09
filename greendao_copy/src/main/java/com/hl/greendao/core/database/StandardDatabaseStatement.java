package com.hl.greendao.core.database;

import android.database.sqlite.SQLiteStatement;

public class StandardDatabaseStatement implements DatabaseStatement {

    private final SQLiteStatement delegate;

    public StandardDatabaseStatement(SQLiteStatement delegate) {
        this.delegate = delegate;
    }

    @Override
    public void clearBindings() {
        delegate.clearBindings();
    }

    @Override
    public void bindLong(int index, long value) {
        delegate.bindLong(index, value);
    }

    @Override
    public void bindString(int index, String value) {
        delegate.bindString(index, value);
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public Object getRawStatement() {
        return delegate;
    }

    @Override
    public long executeInsert() {
        return delegate.executeInsert();
    }
}
