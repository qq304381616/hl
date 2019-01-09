package com.hl.greendao.core.query;

import com.hl.greendao.core.AbstractDao;
import com.hl.greendao.core.Property;
import com.hl.greendao.core.internal.SqlUtils;
import com.hl.utils.L;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder<T> {

    public static boolean LOG_SQL;
    public static boolean LOG_VALUES;

    private final AbstractDao<T, ?> dao;
    private final String tablePrefix;
    private final List<Object> values;
    private final List<Join<T, ?>> joins;
    private final WhereCollector<T> whereCollector;
    private String stringOrderCollation;
    private StringBuilder orderBuilder;
    private Integer limit;
    private Integer offset;
    private boolean distinct;

    public static <T2> QueryBuilder<T2> internalCreate(AbstractDao<T2, ?> dao) {
        return new QueryBuilder<T2>(dao);
    }

    protected QueryBuilder(AbstractDao<T, ?> dao) {
        this(dao, "T");
    }

    protected QueryBuilder(AbstractDao<T, ?> dao, String tablePrefix) {
        this.dao = dao;
        this.tablePrefix = tablePrefix;
        values = new ArrayList<>();
        joins = new ArrayList<>();
        whereCollector = new WhereCollector<T>(dao, tablePrefix);
        stringOrderCollation = " COLLATE NOCASE";
    }

    private void checkOrderBuilder() {
        if (orderBuilder == null) {
            orderBuilder = new StringBuilder();
        } else if (orderBuilder.length() > 0) {
            orderBuilder.append(",");
        }
    }

    public QueryBuilder<T> orderAsc(Property... properties) {
        orderOrDesc(" ASC", properties);
        return this;
    }

    private void orderOrDesc(String ascOrDescWithLeadingSpace, Property... properties) {
        for (Property property : properties) {
            checkOrderBuilder();
            append(orderBuilder, property);
            if (String.class.equals(property.type) && stringOrderCollation != null) {
                orderBuilder.append(stringOrderCollation);
            }
            orderBuilder.append(ascOrDescWithLeadingSpace);
        }
    }

    protected StringBuilder append(StringBuilder builder, Property property) {
        whereCollector.checkProperty(property);
        builder.append(tablePrefix).append(',').append('\'').append(property.columnName).append('\'');
        return builder;
    }

    public Query<T> build() {
        StringBuilder builder = createSelectBuilder();
        int limitPosition = checkAddLimit(builder);
        int offsetPosition = checkAddOffset(builder);

        String sql = builder.toString();
        checkLog(sql);

        return Query.create(dao, sql, values.toArray(), limitPosition, offsetPosition);
    }

    private StringBuilder createSelectBuilder() {
        String select = SqlUtils.createSqlSelect(dao.getTablename(), tablePrefix, dao.getAllColumns(), distinct);
        StringBuilder builder = new StringBuilder(select);
        appendJoinsAndWheres(builder, tablePrefix);

        if (orderBuilder != null && orderBuilder.length() > 0) {
            builder.append(" ORDER BY ").append(orderBuilder);
        }
        return builder;
    }

    private void appendJoinsAndWheres(StringBuilder builder, String tablePrefixOrNull) {
        values.clear();
        for (Join<T, ?> join : joins) {
            builder.append(" JOIN ");
            builder.append('"').append(join.daoDestination.getTablename()).append('"').append(' ');
            builder.append(join.tablePrefix).append(" ON ");
            SqlUtils.appendProperty(builder, join.sourceTablePrefix, join.joinPropertySource).append("=");
            SqlUtils.appendProperty(builder, join.tablePrefix, join.joinPropertyDestination);
        }
        boolean whereAppended = !whereCollector.isEmpty();
        if (whereAppended) {
            builder.append(" WHERE ");
            whereCollector.appendWhereClause(builder, tablePrefixOrNull, values);
        }
        for (Join<T, ?> join : joins) {
            if (!join.whereCollector.isEmpty()) {
                if (!whereAppended) {
                    builder.append(" WHERE ");
                    whereAppended = true;
                } else {
                    builder.append(" AND ");
                }
                join.whereCollector.appendWhereClause(builder, join.tablePrefix, values);
            }
        }
    }

    private int checkAddLimit(StringBuilder builder) {
        int limitPosition = -1;
        if (limit != null) {
            builder.append(" LIMIT ?");
            values.add(limit);
            limitPosition = values.size() - 1;
        }
        return limitPosition;
    }

    private int checkAddOffset(StringBuilder builder) {
        int offsetPosition = -1;
        if (offset != null) {
            if (limit == null) {
                throw new IllegalStateException("Offset cannot be set without limit");
            }
            builder.append(" OFFSET ?");
            values.add(offset);
            offsetPosition = values.size() - 1;
        }
        return offsetPosition;
    }

    private void checkLog(String sql) {
        if (LOG_SQL) {
            L.e("Built SQL for query: " + sql);
        }
        if (LOG_VALUES) {
            L.e("Values for query: " + values);
        }
    }
}
