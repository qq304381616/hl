package com.hl.greendao.core;

import android.database.Cursor;

import java.util.List;

public final class InternalQueryDaoAccess <T>{
    private final AbstractDao<T, ?> dao;

    public InternalQueryDaoAccess(AbstractDao<T, ?> dao) {
        this.dao = dao;
    }

    public List<T> loadAllAndCloseCursor(Cursor cursor) {
        return dao.loadAllAndCloseCursor(cursor);
    }
}
