package com.hl.greendao.core.query;

import com.hl.greendao.core.AbstractDao;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractQueryData<T, Q extends AbstractQuery<T>> {

    final String sql;
    final AbstractDao<T, ?> dao;
    final String[] initialValues;
    final Map<Long, WeakReference<Q>> queriesForThreads;

    public AbstractQueryData(AbstractDao<T, ?> dao, String sql, String[] initialValues) {
        this.sql = sql;
        this.dao = dao;
        this.initialValues = initialValues;
        queriesForThreads = new HashMap<>();
    }

    abstract protected Q createQuery();

    Q forCurrentThread(Q query) {
        if (Thread.currentThread() == query.ownerThread) {
            System.arraycopy(initialValues, 0, query.parmeters, 0, initialValues.length);
            return query;
        } else {
            return forCurrentThread();
        }
    }

    Q forCurrentThread() {
        long threadId = Thread.currentThread().getId();
        synchronized (queriesForThreads) {
            WeakReference<Q> queryRef = queriesForThreads.get(threadId);
            Q query = queryRef != null ? queryRef.get() : null;
            if (query == null) {
                gc();
                query = createQuery();
                queriesForThreads.put(threadId, new WeakReference<Q>(query));
            } else {
                System.arraycopy(initialValues, 0, query.parmeters, 0, initialValues.length);
            }
            return query;
        }
    }

    void gc() {
        synchronized (queriesForThreads) {
            Iterator<Map.Entry<Long, WeakReference<Q>>> iterator = queriesForThreads.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, WeakReference<Q>> entry = iterator.next();
                if (entry.getValue().get() == null) {
                    iterator.remove();
                }
            }
        }
    }
}
