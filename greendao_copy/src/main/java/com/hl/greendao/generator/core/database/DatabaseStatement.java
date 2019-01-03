package com.hl.greendao.generator.core.database;

public interface DatabaseStatement {

    void clearBindings();

    void bindLong(int index, long value);
}
