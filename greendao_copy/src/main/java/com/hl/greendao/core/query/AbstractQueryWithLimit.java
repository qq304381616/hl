package com.hl.greendao.core.query;

import com.hl.greendao.core.AbstractDao;

public abstract class AbstractQueryWithLimit<T> extends AbstractQuery<T>{

    protected final int limitPosition;
    protected final int offsetPosition;

    public AbstractQueryWithLimit(AbstractDao<T,?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
        super(dao, sql, initialValues);
        this.limitPosition = limitPosition;
        this.offsetPosition = offsetPosition;
    }
}
