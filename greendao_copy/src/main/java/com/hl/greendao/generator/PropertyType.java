package com.hl.greendao.generator;

public enum PropertyType {

    Byte(true), Short(true), Int(true), Long(true), Boolean(true), Float(true), Double(true),
    String(false), ByteArray(false), Date(false);

    private final boolean scalar;

    PropertyType(boolean scalar) {
        this.scalar = scalar;
    }

    public boolean isScalar() {
        return scalar;
    }
}
