package com.hl.greendao.core.query;

import com.hl.greendao.core.AbstractDao;
import com.hl.greendao.core.DaoException;
import com.hl.greendao.core.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class WhereCollector<T> {

    private final AbstractDao<T, ?> dao;
    private final String tablePrefix;
    private final List<WhereCondition> whereConditions;

    public WhereCollector(AbstractDao<T, ?> dao, String tablePrefix) {
        this.dao = dao;
        this.tablePrefix = tablePrefix;
        whereConditions = new ArrayList<>();
    }

    public void checkProperty(Property property) {
        if (dao != null) {
            Property[] properties = dao.getProperties();
            boolean found = false;
            for (Property property2 : properties) {
                if (property == property2) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new DaoException("Property '" + property.name + "' is not part of " + dao);
            }
        }
    }

    public void appendWhereClause(StringBuilder builder, String tablePrefixOrNull, List<Object> values) {
        ListIterator<WhereCondition> iterator = whereConditions.listIterator();
        while (iterator.hasNext()) {
            if (iterator.hasPrevious()) {
                builder.append(" AND ");
            }
            WhereCondition c = iterator.next();
            c.appendTo(builder, tablePrefixOrNull);
            c.appendValuesTo(values);
        }
    }
    public boolean isEmpty() {
        return whereConditions.isEmpty();
    }
}
