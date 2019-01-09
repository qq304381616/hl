package com.hl.greendao.core.query;

import android.database.Cursor;

import com.hl.greendao.core.AbstractDao;
import com.hl.greendao.core.DaoException;

import java.util.List;

public class Query<T> extends AbstractQueryWithLimit<T> {

    private final static class QueryData<T2> extends AbstractQueryData<T2, Query<T2>> {
        private final int limitPosition;
        private final int offsetPosition;

        QueryData(AbstractDao<T2, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
            super(dao, sql, initialValues);
            this.limitPosition = limitPosition;
            this.offsetPosition = offsetPosition;
        }

        @Override
        protected Query<T2> createQuery() {
            return new Query<T2>(this, dao, sql, initialValues.clone(), limitPosition, offsetPosition);
        }
    }

    private final QueryData<T> queryData;

    public List<T> list(){
        checkThread();
        Cursor cursor = dao.getDatabase().rawQuery(sql, parmeters);
        return daoAccess.loadAllAndCloseCursor(cursor);
    }

    private Query(QueryData<T> queryData, AbstractDao<T, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
        super(dao, sql, initialValues, limitPosition, offsetPosition);
        this.queryData = queryData;
    }

    public static <T2> Query<T2> create(AbstractDao<T2, ?> dao, String sql, Object[] initialValues, int limitPosition, int offsetPosition) {
        QueryData<T2> queryData = new QueryData<T2>(dao, sql, toStringArray(initialValues), limitPosition, offsetPosition);
        return queryData.forCurrentThread();
    }
}
