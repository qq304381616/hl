package com.hl.greendao.core.identityscope;

public interface IdentityScope <K, T>{

    void clear();

    void put(K key, T entity);

    void putNoLock(K key, T entity);

    void lock();

    void reserveRoom(int count);

    void unlock();

    T get(K key);

    T getNoLock(K key);

    void remove(K key);
}
