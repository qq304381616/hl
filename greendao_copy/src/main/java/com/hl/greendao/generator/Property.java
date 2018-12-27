package com.hl.greendao.generator;

public class Property {

    private final Schema schema;
    private final Entity entity;
    private PropertyType propertyType;
    private final String propertyName;

    private String dbName;
    private String dbType;

    private boolean unique;
    private boolean notNull;
    private boolean nonPrimitiveType;

    private boolean nonDefaultDbName;
    private boolean primaryKey;
    private boolean pkAsc;
    private boolean pkDesc;
    private boolean pkAutoincrement;

    private String constraints ;
    private int ordinal ;
    private String javaType;

    private Index index;

    public Property(Schema schema, Entity entity, PropertyType propertyType, String propertyName) {
        this.schema = schema;
        this.entity = entity;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal ;
    }

    public void init2ndPass() {
        initConstraint();
        if (dbType == null) {
            dbType = schema.mapToDbType(propertyType);
        }
        if (dbName == null) {
            dbName = DaoUtil.dbName(propertyName);
            nonDefaultDbName = false;
        } else if (primaryKey && propertyType == PropertyType.Long && dbName.equals("_id")) {
            nonDefaultDbName = false;
        }

        if (notNull && !nonPrimitiveType) {
            javaType = schema.mapToJavaTypeNotNull(propertyType);
        }else {
            javaType = schema.mapToJavaTypeNullable(propertyType);
        }
    }

    private void initConstraint() {
        StringBuilder builder = new StringBuilder();
        if (primaryKey) {
            builder.append("PRIMARY KEY");
            if (pkAsc) {
                builder.append(" ASC");
            }
            if (pkDesc) {
                builder.append(" DESC");
            }
            if (pkAutoincrement) {
                builder.append(" AUTOINCREMENT");
            }
        }
        if (notNull || (primaryKey && propertyType == PropertyType.String)) {
            builder.append(" NOT NULL");
        }
        if (unique) {
            builder.append(" UNIQUE");
        }
        String s = builder.toString().trim();
        if (s.length() > 0) {
            constraints = s;
        }
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public String getDbName() {
        return dbName;
    }

    public void init3ndPass() {
    }

    public static class PropertyBuilder {
        private final Property property;

        public PropertyBuilder(Schema schema, Entity entity, PropertyType propertyType, String propertyName) {
            this.property = new Property(schema, entity, propertyType, propertyName);
        }

        public PropertyBuilder dbName(String dbName) {
            property.dbName = dbName;
            property.nonDefaultDbName = dbName != null;
            return this;
        }

        public PropertyBuilder dbType(String dbType) {
            property.dbType = dbType;
            return this;
        }

        public PropertyBuilder index(){
            Index index = new Index();
            index.addProperty(property);
            property.entity.addIndex(index);
            return this;
        }

        public PropertyBuilder primaryKey(){
            property.primaryKey = true;
            return this;
        }

        public Property getProperty() {
            return property;
        }

        public PropertyBuilder notNull() {
            property.notNull = true;
            return this;
        }
    }
}
