package com.hl.greendao.generator;

public class Property {

    private final Schema schema;
    private final Entity entity;
    private PropertyType propertyType;
    private final String propertyName;

    private String dbName;
    private String dbType;
    private boolean notNull;

    private boolean nonDefaultDbName;
    private boolean primaryKey;

    public Property(Schema schema, Entity entity, PropertyType propertyType, String propertyName) {
        this.schema = schema;
        this.entity = entity;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
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
