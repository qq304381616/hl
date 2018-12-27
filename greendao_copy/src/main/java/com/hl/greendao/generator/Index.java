package com.hl.greendao.generator;

public class Index extends PropertyOrderList{

    private String name;
    private boolean nonDefaultName;

    public String getName() {
        return name ;
    }

    public void setDefaultName(String name) {
        this.name = name;
        this.nonDefaultName = false;
    }
}
