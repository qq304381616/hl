package com.hl.base.entity;

public class BaseDataEntity extends BasePinyin {
    private int id;
    private String info;
    private String desc;

    public BaseDataEntity(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public BaseDataEntity(String info, String desc) {
        this.info = info;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
