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
}
