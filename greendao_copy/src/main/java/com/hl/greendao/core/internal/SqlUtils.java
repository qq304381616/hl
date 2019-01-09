package com.hl.greendao.core.internal;

import com.hl.greendao.core.DaoException;
import com.hl.greendao.core.Property;
import com.hl.greendao.core.query.QueryBuilder;

public class SqlUtils {

    public static StringBuilder appendProperty(StringBuilder builder, String tablePrefix, Property property) {
        if (tablePrefix != null) {
            builder.append(tablePrefix).append('.');
        }
        builder.append('"').append(property.columnName).append('"');
        return builder;
    }

    public static String createSqlInsert(String insertInfo, String tablename, String[] columns) {
        StringBuilder builder = new StringBuilder(insertInfo);
        builder.append('"').append(tablename).append('"').append(" (");
        appendColumns(builder, columns);
        builder.append(") VALUES (");
        appendPlaceholders(builder, columns.length);
        builder.append(')');
        return builder.toString();
    }

    private static StringBuilder appendPlaceholders(StringBuilder builder, int count) {
        for (int i = 0; i < count; i++) {
            if (i < count - 1) {
                builder.append("?,");
            } else {
                builder.append("?");
            }
        }
        return builder;
    }

    public static StringBuilder appendColumn(StringBuilder builder, String tableAlisa, String column) {
        builder.append(tableAlisa).append(".\"").append(column).append('"');
        return builder;
    }

    public static StringBuilder appendColumns(StringBuilder builder, String tableAlisa, String[] columns) {
        int length = columns.length;
        for (int i = 0; i < length; i++) {
            appendColumn(builder, tableAlisa, columns[i]);
            if (i < length - 1) {
                builder.append(',');
            }
        }
        return builder;
    }

    public static StringBuilder appendColumns(StringBuilder builder, String[] columns) {
        int length = columns.length;
        for (int i = 0; i < length; i++) {
            builder.append('"').append(columns[i]).append('"');
            if (i < length - 1) {
                builder.append(',');
            }
        }
        return builder;
    }

    public static String createSqlSelect(String tablename, String tableAlisa, String[] columns, boolean distinct) {
        if (tableAlisa == null || tableAlisa.length() < 0) {
            throw new DaoException("Table alias required");
        }
        StringBuilder builder = new StringBuilder(distinct ? "SELECT DISTINCT " : "SELECT ");
        SqlUtils.appendColumns(builder, tableAlisa, columns).append(" FROM ");
        builder.append('"').append(tablename).append('"').append(' ').append(tableAlisa).append(' ');
        return builder.toString();
    }
}
