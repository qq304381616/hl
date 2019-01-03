package com.hl.greendao.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.hl.greendao.core.database.Database;
import com.hl.greendao.core.database.DatabaseStatement;
import com.hl.greendao.core.identityscope.IdentityScope;
import com.hl.greendao.core.identityscope.IdentityScopeLong;
import com.hl.greendao.core.internal.DaoConfig;
import com.hl.greendao.core.internal.TableStatements;

public abstract class AbstractDao<T, K> {

    protected final DaoConfig config;
    protected final Database db;
    protected final boolean isStandardSQLite;
    protected final IdentityScope<K, T> identityScope;
    protected final IdentityScopeLong<T> identityScopeLong;
    protected final TableStatements statements;

    protected final AbstractDaoSession session;
    protected final int pkOrdinal;

    public AbstractDao(DaoConfig config) {
        this(config, null);
    }

    public AbstractDao(DaoConfig config, AbstractDaoSession daoSession) {
        this.config = config;
        this.session = daoSession;
        db = config.db;
        isStandardSQLite = db.getRawDatabase() instanceof SQLiteDatabase;
        identityScope = (IdentityScope<K, T>) config.getIdentityScope();
        if (identityScope instanceof IdentityScopeLong) {
            identityScopeLong = (IdentityScopeLong<T>) identityScope;
        } else {
            identityScopeLong = null;
        }
        statements = config.statements;
        pkOrdinal = config.pkProperty != null ? config.pkProperty.ordinal : -1;
    }

    abstract protected T readEntity(Cursor cursor, int offset);

    abstract protected void readEntity(Cursor cursor, T entity, int offset);

    abstract protected K readKey(Cursor cursor, int offset);

    abstract protected void bindValues(DatabaseStatement stmt, T entity);

    abstract protected void bindValues(SQLiteStatement stmt, T entity);

    abstract protected K updateKeyAfterInsert(T entity, long rowId);

    abstract protected K getKey(T entity);

    abstract protected boolean hasKey(T entity);

    abstract protected boolean isEntityUpdateable();
}
