package com.hl.greendao.core.identityscope;

import com.hl.greendao.core.internal.LongHashMap;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantLock;

public class IdentityScopeLong<T> implements IdentityScope<Long, T> {

    private final LongHashMap<Reference<T>> map;
    private final ReentrantLock lock;

    public IdentityScopeLong() {
        map = new LongHashMap<>();
        lock = new ReentrantLock();
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            map.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(Long key, T entity) {
        put2(key, entity);
    }

    public void put2(long key, T entity) {
        lock.lock();
        try {
            map.put(key, new WeakReference<T>(entity));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putNoLock(Long key, T entity) {
        put2NoLock(key, entity);
    }

    public void put2NoLock(long key, T entity) {
        map.put(key, new WeakReference<T>(entity));
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void reserveRoom(int count) {
        map.reserveRoom(count);
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public T get(Long key) {
        return get2(key);
    }

    @Override
    public T getNoLock(Long key) {
        return get2NoLock(key);
    }

    public T get2(long key) {
        lock.lock();
        Reference<T> ref;
        try {
            ref = map.get(key);
        } finally {
            lock.unlock();
        }
        if (ref != null) {
            return ref.get();
        } else {
            return null;
        }
    }

    public T get2NoLock(long key) {
        Reference<T> ref = map.get(key);
        if (ref != null) {
            return ref.get();
        } else {
            return null;
        }
    }
}
