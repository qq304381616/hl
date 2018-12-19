package com.hl.base.entity;

import android.os.Parcelable;

public class BasePinyin extends BaseSort implements Parcelable {
    private String simple;
    private String whole;

    public String getSimple() {
        return simple;
    }

    public void setSimple(String simple) {
        this.simple = simple;
    }

    public String getWhole() {
        return whole;
    }

    public void setWhole(String whole) {
        this.whole = whole;
    }
}
