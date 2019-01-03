package com.hl.greendao.core.identityscope;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class IdentityScopeObject<K, T> implements IdentityScope<K, T> {

    private final HashMap<K, Reference<T>> map;
    private final ReentrantLock lock;

    public IdentityScopeObject() {
        map = new HashMap<>();
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
