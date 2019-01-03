package com.hl.greendao.core.database;

public interface DatabaseStatement {

    void clearBindings();

    void bindLong(int index, long value);

    void bindString(int index, String value);
}
