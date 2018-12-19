package com.hl.systeminfo.appinfo;

import com.hl.base.entity.BasePinyin;

public class AppEntity extends BasePinyin {

    private String name;
    private android.content.pm.PackageInfo PackageInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public android.content.pm.PackageInfo getPackageInfo() {
        return PackageInfo;
    }

    public void setPackageInfo(android.content.pm.PackageInfo packageInfo) {
        PackageInfo = packageInfo;
    }
}
