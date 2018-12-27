package com.hl.greendao.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
    private final Schema schema;
    private final String className;

    private String dbName;
    private boolean nonDefaultDbName;
    private String classNameDao;
    private String classNameTest;
    private String javaPackage; // == javaPackageDao == javaPackageTest
    private Property pkProperty;
    private String pkType;

    private final List<Property> properties;
    private final List<Property> propertiesPk;
    private final List<Property> propertiesNonPk;
    private final List<Index> multiIndexes;
    private final Set<String> propertyNames;
    private final List<Index> indexes;
    private List<Property> propertiesColumns;
    private final List<ToOne> toOneRelations;

    private Boolean active;
    private Boolean hasKeepSections;

    public Entity(Schema schema, String className) {
        this.schema = schema;
        this.className = className;
        propertyNames = new HashSet<>();
        properties = new ArrayList<>();
        propertiesPk = new ArrayList<>();
        propertiesNonPk = new ArrayList<>();
        multiIndexes = new ArrayList<>();
        indexes = new ArrayList<>();
        toOneRelations = new ArrayList<>();
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

    public void init2ndPass() {
        init2ndPassNamesWithDefault();

        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);
            property.setOrdinal(i);
            property.init2ndPass();
            if (property.isPrimaryKey()) {
                propertiesPk.add(property);
            } else {
                propertiesNonPk.add(property);
            }
        }

        for (int i = 0; i < indexes.size(); i++) {
            final Index index = indexes.get(i);
            final int propertiesSize = index.getProperties().size();
            if (propertiesSize == 1) {
                final Property property = index.getProperties().get(0);
                property.setIndex(index);
            } else if (propertiesSize > 1) {
                multiIndexes.add(index);
            }
        }

        if (propertiesPk.size() == 1) {
            pkProperty = propertiesPk.get(0);
            pkType = schema.mapToJavaTypeNullable(pkProperty.getPropertyType());
        } else {
            pkType = "Void";
        }


        // TODO toOne toMany。 暂不实现
        propertiesColumns = new ArrayList<>(properties);
        for (ToOne toOne : toOneRelations) {
        }

        if (hasKeepSections == null) {
            hasKeepSections = schema.isHasKeepSectionsByDefault();
        }

        init2ndPassIndexNamesWithDefault();

        // TODO  contentProviders 暂不实现
    }

    private void init2ndPassIndexNamesWithDefault() {
        for (int i = 0; i < indexes.size(); i++) {
            Index index = indexes.get(i);
            if (index.getName() == null) {
                String indexName = "IDX_" + dbName;
                List<Property> properties = index.getProperties();
                for (int j = 0; j < properties.size(); j++) {
                    Property property = properties.get(j);
                    indexName += "_" + property.getDbName();
                    if ("DESC".equalsIgnoreCase(index.getPropertiesOrder().get(j))) {
                        indexName += "_DESC";
                    }
                }
                index.setDefaultName(indexName);
            }
        }
    }

    private void init2ndPassNamesWithDefault() {
        if (dbName == null) {
            dbName = DaoUtil.dbName(className);
            nonDefaultDbName = false;
        }
        if (classNameDao == null) {
            classNameDao = className + "Dao";
        }
        if (classNameTest == null) {
            classNameTest = className + "Test";
        }
        if (javaPackage == null) {
            javaPackage = schema.getDefaultJavaPackage();
        }
    }

    public void init3rdPass() {
        for (Property property : properties) {
            property.init3ndPass();
        }
        init3rdPassRelations();
        init3rdPassAdditionalImports();
    }

    private void init3rdPassRelations() {
        // TODO   暂不实现
    }

    private void init3rdPassAdditionalImports() {
        // TODO   暂不实现
    }

}
