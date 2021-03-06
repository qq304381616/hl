package com.hl.greendao.core.internal;

import com.hl.greendao.core.database.Database;
import com.hl.greendao.core.database.DatabaseStatement;

public class TableStatements {
    private final Database db;
    private final String tablename;
    private final String [] allColumns;
    private final String [] pkColumns;

    private DatabaseStatement insertStatement;
    private DatabaseStatement insertOrReplaceStatement;
    private DatabaseStatement updateStatement;
    private DatabaseStatement deleteStatement;
    private DatabaseStatement countStatement;

    private volatile String selectAll;
    private volatile String selectByKey;
    private volatile String selectByRowId;
    private volatile String selectByKeys;

    public TableStatements(Database db, String tablename, String[] allColumns, String[] pkColumns) {
        this.db = db;
        this.tablename = tablename;
        this.allColumns = allColumns;
        this.pkColumns = pkColumns;
    }

    public DatabaseStatement getInsertStatement() {
        if (insertStatement == null) {
            String sql = SqlUtils.createSqlInsert("INSERT INTO ", tablename, allColumns);
            DatabaseStatement s = db.compileStatement(sql);
            synchronized (this) {
                if (insertStatement == null) {
                    insertStatement = s;
                }
            }
            if (insertStatement != s) {
                s.close();
            }
        }
        return insertStatement;
    }

    public DatabaseStatement getDeleteStatement() {
        if (deleteStatement == null) {
            String sql = SqlUtils.createSqlDelete(tablename, pkColumns);
            DatabaseStatement s = db.compileStatement(sql);
            synchronized (this) {
                if (deleteStatement == null) {
                    deleteStatement = s;
                }
            }
            if (deleteStatement != s) {
                s.close();
            }
        }
        return deleteStatement;
    }
}
