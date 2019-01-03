package com.hl.greendao.core.database;

import android.database.SQLException;

public interface Database {

    void execSQL(String sql) throws SQLException;

    Object getRawDatabase();

}
