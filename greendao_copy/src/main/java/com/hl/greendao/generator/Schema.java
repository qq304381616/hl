package com.hl.greendao.generator;

import java.util.ArrayList;
import java.util.List;

public class Schema {
    public static final String DEFAULT_NAME = "default";

    private final int version;
    private final String name;
    private final String defaultJavaPackage;
    private final List<Entity> entities;

    public Schema(String name, int version, String defaultJavaPackage) {
        this.version = version;
        this.name = name;
        this.defaultJavaPackage = defaultJavaPackage;
        entities = new ArrayList<>();

    }

    public Schema(int version, String defaultJavaPackage) {
        this(DEFAULT_NAME, version, defaultJavaPackage);
    }

    public Entity addEntity(String className) {
        Entity entity = new Entity(this, className);
        entities.add(entity);
        return entity;
    }
}
