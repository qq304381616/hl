package com.hl.base.entity;

public class BaseDataEntity extends BaseSort implements Comparable<BaseDataEntity> {
    private int id;
    private String info;

    public BaseDataEntity(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int compareTo(BaseDataEntity o) {
        return getFirst().compareTo(o.getFirst());
    }
}
