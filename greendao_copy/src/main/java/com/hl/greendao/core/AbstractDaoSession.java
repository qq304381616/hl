package com.hl.greendao.core;

import com.hl.greendao.core.database.Database;

import java.util.HashMap;
import java.util.Map;

public class AbstractDaoSession {
    private final Database db;
    private final Map<Class<?>, AbstractDao<?, ?>> entityToDao;

    public AbstractDaoSession(Database db) {
        this.db = db;
        this.entityToDao = new HashMap<>();
    }

    protected <T> void registerDao(Class<T> entityClass, AbstractDao<T, ?> dao) {
        entityToDao.put(entityClass, dao);
    }
}
