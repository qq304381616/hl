package com.hl.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    public static final String SP_FILE = "sp_file";

    public static final String KEY = "key";


    private SpUtils() {
    }

    public static boolean putString(String name, Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(String name, Context context, String key) {
        return getString(name, context, key, null);
    }

    public static String getString(String name, Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static boolean putInt(String name, Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(String name, Context context, String key) {
        return getInt(name, context, key, -1);
    }

    public static int getInt(String name, Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    public static boolean putLong(String name, Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(String name, Context context, String key) {
        return getLong(name, context, key, -1);
    }

    public static long getLong(String name, Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    public static boolean putFloat(String name, Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(String name, Context context, String key) {
        return getFloat(name, context, key, -1);
    }

    public static float getFloat(String name, Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    public static boolean putBoolean(String name, Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(String name, Context context, String key) {
        return getBoolean(name, context, key, false);
    }

    public static boolean getBoolean(String name, Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /********************************************************************************************/

    public static boolean setIsFirstAutHelper(Context context, boolean value) {
        return putBoolean(SP_FILE, context, KEY, value);
    }


    public static boolean getIsFirstAuHelper(Context context, boolean defaultValue) {
        return getBoolean(SP_FILE, context, KEY, defaultValue);
    }

}
