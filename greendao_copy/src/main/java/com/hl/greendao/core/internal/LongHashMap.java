package com.hl.greendao.core.internal;

import java.util.Arrays;

public final class LongHashMap<T> {

    public void reserveRoom(int entryCount) {
        setCapacity(entryCount * 5 / 3);
    }

    public T get(long key) {
        final int index = ((((int) (key >>> 32)) ^ ((int) (key))) & 0x7fffffff) % capacity;
        for (Entry<T> entry = table[index]; entry != null; entry = entry.next) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        return null;
    }

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
    private int capacity;
    private int threshold;

    public LongHashMap() {
        this(16);
    }

    public LongHashMap(int capacity) {
        this.capacity = capacity;
        this.threshold = capacity * 4 / 3;
        this.table = new Entry[capacity];
    }

    public void clear() {
        size = 0;
        Arrays.fill(table, null);
    }

    public T put(long key, T value) {
        final int index = ((((int) (key >>> 32)) ^ ((int) (key))) & 0x7fffffff) % capacity;
        final Entry<T> entryOriginal = table[index];
        for (Entry<T> entry = entryOriginal; entry != null; entry = entry.next) {
            if (entry.key == key) {
                T oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }
        table[index] = new Entry<T>(key, value, entryOriginal);
        size++;
        if (size > threshold) {
            setCapacity(2 * capacity);
        }
        return null;
    }

    public void setCapacity(int newCapacity) {
        Entry<T>[] newTable = new Entry[newCapacity];
        int length = table.length;
        for (int i = 0; i < length; i++) {
            Entry<T> entry = table[i];
            while (entry != null) {
                long key = entry.key;
                int index = ((((int) (key >>> 32)) ^ ((int) (key))) & 0x7fffffff) % newCapacity;

                Entry<T> originalNext = entry.next;
                entry.next = newTable[index];
                newTable[index] = entry;
                entry = originalNext;
            }
        }
        table = newTable;
        capacity = newCapacity;
        threshold = newCapacity * 4 / 3;
    }
}
