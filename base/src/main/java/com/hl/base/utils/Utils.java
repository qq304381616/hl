package com.hl.base.utils;

import android.text.TextUtils;

public class Utils {

    public static String getLetter(String name) {
        if (TextUtils.isEmpty(name)) return "#";
        String pinyin = new Pinyin().getPinYin(name).substring(0, 1).toUpperCase();
        return pinyin.matches("[A-Z]") ? pinyin : "#";
    }
}
