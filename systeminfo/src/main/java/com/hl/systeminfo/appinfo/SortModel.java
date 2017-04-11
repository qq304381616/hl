package com.hl.systeminfo.appinfo;

public class SortModel {

    private String name;
    private String sortLetters; //显示数据拼音的首字母
    private android.content.pm.PackageInfo PackageInfo;

    public SortModel() {

    }

    public SortModel(String name, String sortLetters) {
        this.name = name;
        this.sortLetters = sortLetters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public android.content.pm.PackageInfo getPackageInfo() {
        return PackageInfo;
    }

    public void setPackageInfo(android.content.pm.PackageInfo packageInfo) {
        PackageInfo = packageInfo;
    }
}
