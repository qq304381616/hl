package com.hl.greendao.generator;

public class DaoUtil {

    public static String capFirst(String string){
        return Character.toUpperCase(string.charAt(0)) + (string.length() > 1 ? string.substring(1) : "");
    }
}
