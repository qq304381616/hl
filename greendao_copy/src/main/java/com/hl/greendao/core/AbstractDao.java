package com.hl.greendao.core;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.hl.greendao.core.database.Database;
import com.hl.greendao.core.database.DatabaseStatement;
import com.hl.greendao.core.identityscope.IdentityScope;
import com.hl.greendao.core.identityscope.IdentityScopeLong;
import com.hl.greendao.core.internal.DaoConfig;
import com.hl.greendao.core.internal.FastCursor;
import com.hl.greendao.core.internal.TableStatements;
import com.hl.greendao.core.query.QueryBuilder;
import com.hl.utils.L;

import java.util.ArrayList;
import java.util.List;

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

    public long insert(T entity) {
        return executeInsert(entity, statements.getInsertStatement(), true);
    }

    private long executeInsert(T entity, DatabaseStatement stmt, boolean setKeyAndAttach) {
        long rowId;
        if (db.isDbLockedByCurrentThread()) {
            rowId = insertInsideTx(entity, stmt);
        } else {
            db.beginTransaction();
            try {
                rowId = insertInsideTx(entity, stmt);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        if (setKeyAndAttach) {
            updateKeyafterInsertAndAttach(entity, rowId, true);
        }
        return rowId;
    }

    private long insertInsideTx(T entity, DatabaseStatement stmt) {
        synchronized (stmt) {
            if (isStandardSQLite) {
                SQLiteStatement rawStmt = (SQLiteStatement) stmt.getRawStatement();
                bindValues(rawStmt, entity);
                return rawStmt.executeInsert();
            } else {
                bindValues(stmt, entity);
                return stmt.executeInsert();
            }
        }
    }

    protected void updateKeyafterInsertAndAttach(T entity, long rowId, boolean lock) {
        if (rowId != -1) {
            K key = updateKeyAfterInsert(entity, rowId);
            attachEntity(key, entity, lock);
        } else {
            L.e("Could not insert row (executeInsert returned -1)");
        }
    }

    protected final void attachEntity(K key, T entity, boolean lock) {
        attachEntity(entity);
        if (identityScope != null && key != null) {
            if (lock) {
                identityScope.put(key, entity);
            } else {
                identityScope.putNoLock(key, entity);
            }
        }
    }

    protected void attachEntity(T entity) {

    }

    public QueryBuilder<T> queryBuilder() {
        return QueryBuilder.internalCreate(this);
    }

    protected List<T> loadAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }

    protected List<T> loadAllFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        if (count == 0) {
            return new ArrayList<T>();
        }
        List<T> list = new ArrayList<>(count);
        CursorWindow window = null;
        boolean useFastCursor = false;
        if (cursor instanceof CrossProcessCursor) {
            window = ((CrossProcessCursor) cursor).getWindow();
            if (window != null) {
                if (window.getNumRows() == count) {
                    cursor = new FastCursor(window);
                    useFastCursor = true;
                } else {
                    L.e("Window vs. result size: " + window.getNumRows() + "/" + count);
                }
            }
        }
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                if (!useFastCursor && window != null && identityScope != null) {
                    loadAllUnlockOnWindowBounds(cursor, window, list);
                } else {
                    do {
                        list.add(loadCurrent(cursor, 0, false));
                    } while (cursor.moveToNext());
                }
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }

    private void loadAllUnlockOnWindowBounds(Cursor cursor, CursorWindow window, List<T> list) {
        int windowEnd = window.getStartPosition() + window.getNumRows();
        for (int row = 0; ; row++) {
            list.add(loadCurrent(cursor, 0, false));
            row++;
            if (row >= windowEnd) {
                window = moveToNextUnlocked(cursor);
                if (window == null) {
                    break;
                }
                windowEnd = window.getStartPosition() + window.getNumRows();
            } else {
                if (cursor.moveToNext()) {
                    break;
                }
            }
        }
    }

    final protected T loadCurrent(Cursor cursor, int offset, boolean lock) {
        if (identityScopeLong != null) {
            if (offset != 0) {
                if (cursor.isNull(pkOrdinal + offset)) {
                    return null;
                }
            }
            long key = cursor.getLong(pkOrdinal + offset);
            T entity = lock ? identityScopeLong.get2(key) : identityScopeLong.get2NoLock(key);
            if (entity != null) {
                return entity;
            } else {
                entity = readEntity(cursor, offset);
                attachEntity(entity);
                if (lock) {
                    identityScopeLong.put2(key, entity);
                } else {
                    identityScopeLong.put2NoLock(key, entity);
                }
                return entity;
            }
        } else if (identityScope != null) {
            K key = readKey(cursor, offset);
            if (offset != 0 && key == null) {
                return null;
            }
            T entity = lock ? identityScope.get(key) : identityScope.getNoLock(key);
            if (entity != null) {
                return entity;
            } else {
                entity = readEntity(cursor, offset);
                attachEntity(key, entity, lock);
                return entity;
            }
        } else {
            if (offset != 0) {
                K key = readKey(cursor, offset);
                if (key == null) {
                    return null;
                }
            }
            T entity = readEntity(cursor, offset);
            attachEntity(entity);
            return entity;
        }
    }

    private CursorWindow moveToNextUnlocked(Cursor cursor) {
        identityScope.unlock();
        try {
            if (cursor.moveToNext()) {
                return ((CrossProcessCursor) cursor).getWindow();
            } else {
                return null;
            }
        } finally {
            identityScope.lock();
        }
    }

    public Property[] getProperties() {
        return config.properties;
    }

    public String getTablename() {
        return config.tablename;
    }

    public Database getDatabase() {
        return db;
    }

    public String[] getAllColumns() {
        return config.allColumns;
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
