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
    private final String prefix;
    private String defaultJavaPackageDao; // == defaultJavaPackageDao == defaultJavaPackageTest
    private String defaultJavaPackageTest; // == defaultJavaPackageDao == defaultJavaPackageTest
    private boolean hasKeepSectionsByDefault;
    private boolean useActiveEntitiesByDefault;

    public Schema(String name, int version, String defaultJavaPackage) {
        this.name = name;
        this.prefix = name.equals(DEFAULT_NAME) ? "" : DaoUtil.capFirst(name);
        this.version = version;
        this.defaultJavaPackage = defaultJavaPackage;
        entities = new ArrayList<>();
    }

    public Schema(int version, String defaultJavaPackage) {
        this(DEFAULT_NAME, version, defaultJavaPackage);
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
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
        defaultJavaPackageDao = defaultJavaPackage;
        defaultJavaPackageTest = defaultJavaPackage;

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

    public String getDefaultJavaPackageDao() {
        return defaultJavaPackageDao;
    }

    public String getDefaultJavaPackageTest() {
        return defaultJavaPackageTest;
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
