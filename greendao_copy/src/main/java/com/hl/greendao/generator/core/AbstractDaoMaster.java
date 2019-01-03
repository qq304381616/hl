package com.hl.greendao.generator.core;

import com.hl.greendao.generator.core.database.Database;
import com.hl.greendao.generator.core.identityscope.IdentityScopeType;
import com.hl.greendao.generator.core.internal.DaoConfig;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDaoMaster {

    protected final Database db;
    protected final int schemaVersion;
    protected final Map<Class<? extends AbstractDao<?,?>>, DaoConfig> daoConfigMap;

    public AbstractDaoMaster(Database db, int schemaVersion) {
        this.db = db;
        this.schemaVersion = schemaVersion;
        this.daoConfigMap = new HashMap<Class<? extends AbstractDao<?,?>>,DaoConfig>();
    }

    protected void registerDaoClass(Class<? extends AbstractDao<?, ?>> daoClass) {
        DaoConfig daoConfig = new DaoConfig(db, daoClass);
        daoConfigMap.put(daoClass, daoConfig);
    }

    public Database getDatabase() {
        return db ;
    }

    public abstract AbstractDaoSession newSession();

    public abstract AbstractDaoSession newSession(IdentityScopeType type);
}
