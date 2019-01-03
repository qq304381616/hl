package com.hl.greendao.core.internal;

import java.util.Arrays;

public final class LongHashMap<T> {

    final static class Entry<T> {
        final long key;
        T value;
        Entry<T> next;

        Entry(long key, T value, Entry<T> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int size;
    private Entry<T>[] table;

    public void clear() {
        size = 0;
        Arrays.fill(table, null);
    }
}
