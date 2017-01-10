package com.hl.utils;

import android.os.Looper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * 是否主线程
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 判断email格式是否正确
     ***/
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * @param text
     * @Title: isNotCharacters
     * @Description: 校验是否不含有汉字
     * @return: boolean
     */
    public static boolean isNotCharacters(String text) {
        String regex = "^[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(text);
        return match.matches();
    }

}
