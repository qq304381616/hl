package com.hl.base.utils;

import com.hl.base.entity.BaseSort;

import java.util.Comparator;

public class FirstComparator implements Comparator<BaseSort> {

    @Override
    public int compare(BaseSort o1, BaseSort o2) {
        return o1.getFirst().compareTo(o2.getFirst());
    }
}