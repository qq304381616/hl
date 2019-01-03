package com.hl.greendao.core;

public class Property {
    public final int ordinal ;
    public final Class<?> type;
    public final String name;
    public final boolean primaryKey;
    public final String columnName;

    public Property(int ordinal, Class<?> type, String name, boolean primaryKey, String columnName) {
        this.ordinal = ordinal;
        this.type = type;
        this.name = name;
        this.primaryKey = primaryKey;
        this.columnName = columnName;
    }


}
