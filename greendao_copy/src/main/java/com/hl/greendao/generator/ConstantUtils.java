package com.hl.greendao.generator;

import java.util.HashMap;
import java.util.Map;

public class ConstantUtils {

    public static Map<PropertyType, String> propertyToDbType;
    public static Map<PropertyType, String> propertyToJavaTypeNotNull;
    public static Map<PropertyType, String> propertyToJavaTypeNullable;

    static {
        propertyToDbType = new HashMap<>();
        propertyToDbType.put(PropertyType.Boolean, "INTEGER");
        propertyToDbType.put(PropertyType.Byte, "INTEGER");
        propertyToDbType.put(PropertyType.Short, "INTEGER");
        propertyToDbType.put(PropertyType.Int, "INTEGER");
        propertyToDbType.put(PropertyType.Long, "INTEGER");
        propertyToDbType.put(PropertyType.Float, "REAL");
        propertyToDbType.put(PropertyType.Double, "REAL");
        propertyToDbType.put(PropertyType.String, "TEXT");
        propertyToDbType.put(PropertyType.ByteArray, "BLOB");
        propertyToDbType.put(PropertyType.Date, "INTEGER");

        propertyToJavaTypeNotNull = new HashMap<>();
        propertyToJavaTypeNotNull.put(PropertyType.Boolean, "boolean");
        propertyToJavaTypeNotNull.put(PropertyType.Byte, "byte");
        propertyToJavaTypeNotNull.put(PropertyType.Short, "short");
        propertyToJavaTypeNotNull.put(PropertyType.Int, "int");
        propertyToJavaTypeNotNull.put(PropertyType.Long, "long");
        propertyToJavaTypeNotNull.put(PropertyType.Float, "float");
        propertyToJavaTypeNotNull.put(PropertyType.Double, "double");
        propertyToJavaTypeNotNull.put(PropertyType.String, "String");
        propertyToJavaTypeNotNull.put(PropertyType.ByteArray, "byte[]");
        propertyToJavaTypeNotNull.put(PropertyType.Date, "java.util.Date");

        propertyToJavaTypeNullable = new HashMap<>();
        propertyToJavaTypeNullable.put(PropertyType.Boolean, "Boolean");
        propertyToJavaTypeNullable.put(PropertyType.Byte, "Byte");
        propertyToJavaTypeNullable.put(PropertyType.Short, "Short");
        propertyToJavaTypeNullable.put(PropertyType.Int, "Integer");
        propertyToJavaTypeNullable.put(PropertyType.Long, "Long");
        propertyToJavaTypeNullable.put(PropertyType.Float, "Float");
        propertyToJavaTypeNullable.put(PropertyType.Double, "Double");
        propertyToJavaTypeNullable.put(PropertyType.String, "String");
        propertyToJavaTypeNullable.put(PropertyType.ByteArray, "byte[]");
        propertyToJavaTypeNullable.put(PropertyType.Date, "java.util.Date");
    }
}
