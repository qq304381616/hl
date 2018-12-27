package com.hl.greendao.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
    private final Schema schema;
    private final String className;

    private final List<Property> properties;
    private final Set<String> propertyNames;
    private final List<Index> indexes;

    public Entity(Schema schema, String className) {
        this.schema = schema;
        this.className = className;
        propertyNames = new HashSet<>();
        properties = new ArrayList<>();
        indexes = new ArrayList<>();
    }

    public Property.PropertyBuilder addIdProperty() {
        Property.PropertyBuilder builder = addLongProperty("id");
        builder.dbName("_id").primaryKey();
        return builder;
    }

    public Entity addIndex(Index index) {
        indexes.add(index);
        return this;
    }

    public Property.PropertyBuilder addLongProperty(String propertyName) {
        return addProperty(PropertyType.Long, propertyName);
    }

    public Property.PropertyBuilder addIntProperty(String propertyName) {
        return addProperty(PropertyType.Int, propertyName);
    }

    public Property.PropertyBuilder addProperty(PropertyType propertyType, String propertyName) {
        if (!propertyNames.add(propertyName)) {
            throw new RuntimeException("Property already defined" + propertyName);
        }
        Property.PropertyBuilder builder = new Property.PropertyBuilder(schema, this, propertyType, propertyName);
        properties.add(builder.getProperty());
        return builder;
    }
}
