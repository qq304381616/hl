package com.hl.greendao.core.identityscope;

import com.hl.greendao.core.internal.LongHashMap;

import java.lang.ref.Reference;
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
}
