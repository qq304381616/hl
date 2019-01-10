package com.hl.greendao.core.database;

public interface DatabaseStatement {

    void execute();

    void clearBindings();

    void bindLong(int index, long value);

    void bindString(int index, String value);

    void close();

    Object getRawStatement();

    long executeInsert();
}
