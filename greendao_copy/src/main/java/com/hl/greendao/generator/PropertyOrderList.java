package com.hl.greendao.generator;

import java.util.ArrayList;
import java.util.List;

public class PropertyOrderList {

    private List<Property> properties;
    private List<String> propertiesOrder;

    public PropertyOrderList() {
        properties = new ArrayList<>();
        propertiesOrder = new ArrayList<>();
    }

    public void addProperty(Property property) {
        properties.add(property);
        propertiesOrder.add(null);
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<String> getPropertiesOrder() {
        return propertiesOrder;
    }
}
