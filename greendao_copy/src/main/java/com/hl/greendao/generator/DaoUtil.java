package com.hl.greendao.generator;

public class DaoUtil {

    public static String dbName(String javaName) {
        StringBuilder builder = new StringBuilder(javaName);
        for (int i = 1; i < builder.length(); i++) {
            boolean lastWasUpper = Character.isUpperCase(builder.charAt(i-1));
            boolean isUpper = Character.isUpperCase(builder.charAt(i));
            if (isUpper && !lastWasUpper) {
                builder.insert(i, '_');
                i++;
            }
        }
        return builder.toString().toUpperCase();
    }

    public static String capFirst(String string){
        return Character.toUpperCase(string.charAt(0)) + (string.length() > 1 ? string.substring(1) : "");
    }


}
