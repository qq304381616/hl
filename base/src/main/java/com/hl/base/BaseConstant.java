package com.hl.base;


import com.hl.base.entity.BaseDataEntity;

import java.util.ArrayList;
import java.util.List;

public class BaseConstant {

    public static List<BaseDataEntity> getData() {
        return getData("item", 12);
    }

    public static List<BaseDataEntity> getData(String name) {
        return getData(name, 12);
    }

    public static List<BaseDataEntity> getData(String name, int count) {
        List<BaseDataEntity> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add(new BaseDataEntity(1, name + " " + i));
        }
        return data;
    }
}
