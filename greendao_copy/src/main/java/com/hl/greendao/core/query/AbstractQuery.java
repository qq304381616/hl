package com.hl.greendao.core.query;

import com.hl.greendao.core.AbstractDao;
import com.hl.greendao.core.DaoException;
import com.hl.greendao.core.InternalQueryDaoAccess;

public abstract class AbstractQuery<T> {
    protected final AbstractDao<T, ?> dao;
    protected final InternalQueryDaoAccess<T> daoAccess;
    protected final String sql;
    protected final String[] parmeters;
    protected final Thread ownerThread;

    public AbstractQuery(AbstractDao<T, ?> dao, String sql, String[] parmeters) {
        this.dao = dao;
        this.daoAccess = new InternalQueryDaoAccess<T>(dao);
        this.sql = sql;
        this.parmeters = parmeters;
        ownerThread = Thread.currentThread();
    }

    protected static String[] toStringArray(Object[] values) {
        int length = values.length;
        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            Object object = values[i];
            if (object != null) {
                strings[i] = object.toString();
            } else {
                strings[i] = null;
            }
        }
        return strings;
    }

    protected void checkThread(){
        if (Thread.currentThread() != ownerThread) {
            throw new DaoException("Method may be called only in owner thread, use forCurrentThread to get and instance for this thread");
        }
    }
}
