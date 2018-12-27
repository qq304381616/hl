package com.hl.greendao.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Schema {
    public static final String DEFAULT_NAME = "default";

    private final int version;
    private final String name;
    private final String defaultJavaPackage; // == defaultJavaPackageDao == defaultJavaPackageTest

    private final List<Entity> entities;
    private boolean hasKeepSectionsByDefault ;
    private boolean useActiveEntitiesByDefault ;

    public Schema(String name, int version, String defaultJavaPackage) {
        this.version = version;
        this.name = name;
        this.defaultJavaPackage = defaultJavaPackage;
        entities = new ArrayList<>();

    }

    public Schema(int version, String defaultJavaPackage) {
        this(DEFAULT_NAME, version, defaultJavaPackage);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity addEntity(String className) {
        Entity entity = new Entity(this, className);
        entities.add(entity);
        return entity;
    }

    public void init2ndPass() {
        for (Entity entity : entities) {
            entity.init2ndPass();
        }
    }

    public void init3rdPass() {
        for (Entity entity : entities) {
            entity.init3rdPass();
        }
    }

    public String getDefaultJavaPackage() {
        return defaultJavaPackage;
    }

    public String mapToDbType(PropertyType propertyType) {
        return mapType(ConstantUtils.propertyToDbType, propertyType);
    }

    public String mapToJavaTypeNotNull(PropertyType propertyType) {
        return mapType(ConstantUtils.propertyToJavaTypeNotNull, propertyType);
    }

    public String mapToJavaTypeNullable(PropertyType propertyType) {
        return mapType(ConstantUtils.propertyToJavaTypeNullable, propertyType);
    }

    private String mapType(Map<PropertyType, String> map, PropertyType propertyType) {
        String dbType = map.get(propertyType);
        if (dbType == null) {
            throw new IllegalStateException("No mapping for " + propertyType);
        }
        return dbType;
    }

    public Boolean isUseActiveEntitiesByDefault() {
        return useActiveEntitiesByDefault;
    }

    public Boolean isHasKeepSectionsByDefault() {
        return hasKeepSectionsByDefault;
    }
}
