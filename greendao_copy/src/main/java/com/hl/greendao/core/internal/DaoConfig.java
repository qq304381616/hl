package com.hl.greendao.core.internal;

import com.hl.greendao.core.AbstractDao;
import com.hl.greendao.core.DaoException;
import com.hl.greendao.core.Property;
import com.hl.greendao.core.database.Database;
import com.hl.greendao.core.identityscope.IdentityScope;
import com.hl.greendao.core.identityscope.IdentityScopeLong;
import com.hl.greendao.core.identityscope.IdentityScopeObject;
import com.hl.greendao.core.identityscope.IdentityScopeType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class DaoConfig implements Cloneable {

    public final Database db;
    public final String tablename;
    public final Property[] properties;

    public final String[] allColumns;
    public final String[] pkColumns;
    public final String[] nonPkColumns;

    public final Property pkProperty;
    public final boolean keyIsNumeric;
    public final TableStatements statements;

    private IdentityScope<?, ?> identityScope;

    public DaoConfig(Database db, Class<? extends AbstractDao<?, ?>> daoClass) {
        this.db = db;
        try {
            this.tablename = (String) daoClass.getField("TABLENAME").get(null);
            Property[] properties = reflectProperties(daoClass);
            this.properties = properties;

            allColumns = new String[properties.length];
            List<String> pkColumnList = new ArrayList<>();
            List<String> nonPkColumnList = new ArrayList<>();
            Property lastPkProperty = null;
            for (int i = 0; i < properties.length; i++) {
                Property property = properties[i];
                String name = property.columnName;
                allColumns[i] = name;
                if (property.primaryKey) {
                    pkColumnList.add(name);
                    lastPkProperty = property;
                } else {
                    nonPkColumnList.add(name);
                }
            }

            String[] nonPkColumnsArray = new String[nonPkColumnList.size()];
            nonPkColumns = nonPkColumnList.toArray(nonPkColumnsArray);
            String[] pkColumnsArray = new String[pkColumnList.size()];
            pkColumns = pkColumnList.toArray(pkColumnsArray);

            pkProperty = pkColumns.length == 1 ? lastPkProperty : null;
            statements = new TableStatements(db, tablename, allColumns, pkColumns);

            if (pkProperty != null) {
                Class<?> type = pkProperty.type;
                keyIsNumeric = type.equals(long.class) || type.equals(Long.class) || type.equals(int.class)
                        || type.equals(Integer.class) || type.equals(short.class) || type.equals(Short.class)
                        || type.equals(byte.class) || type.equals(Byte.class);
            } else {
                keyIsNumeric = false;
            }

        } catch (Exception e) {
            throw new DaoException("Could not init DaoConfig", e);
        }
    }

    public IdentityScope<?, ?> getIdentityScope() {
        return identityScope;
    }

    private static Property[] reflectProperties(Class<? extends AbstractDao<?, ?>> daoClass) throws ClassNotFoundException, IllegalAccessException {
        Class<?> propertiesClass = Class.forName(daoClass.getName() + "$Properties");
        Field[] fields = propertiesClass.getDeclaredFields();

        ArrayList<Property> propertyList = new ArrayList<>();
        final int modifiermask = Modifier.STATIC | Modifier.PUBLIC;
        for (Field field : fields) {
            if ((field.getModifiers() & modifiermask) == modifiermask) {
                Object fieldValue = field.get(null);
                if (fieldValue instanceof Property) {
                    propertyList.add((Property) fieldValue);
                }
            }
        }
        Property[] properties = new Property[propertyList.size()];
        for (Property property : propertyList) {
            if (properties[property.ordinal] != null) {
                throw new DaoException("Duplicate property ordinals");
            }
            properties[property.ordinal] = property;
        }
        return properties;
    }

    public DaoConfig(DaoConfig source) {
        db = source.db;
        tablename = source.tablename;
        properties = source.properties;
        allColumns = source.allColumns;
        pkColumns = source.pkColumns;
        nonPkColumns = source.nonPkColumns;
        pkProperty = source.pkProperty;
        statements = source.statements;
        keyIsNumeric = source.keyIsNumeric;
    }

    @Override
    public DaoConfig clone() {
        return new DaoConfig(this);
    }

    public void initIdentityScope(IdentityScopeType type) {
        if (type == IdentityScopeType.None) {
            identityScope = null;
        } else if (type == IdentityScopeType.Session) {
            if (keyIsNumeric) {
                identityScope = new IdentityScopeLong();
            } else {
                identityScope = new IdentityScopeObject();
            }
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    public void clearIdentityScope() {
        IdentityScope<?, ?> identityScope = this.identityScope;
        if (identityScope != null) {
            identityScope.clear();
        }
    }
}
