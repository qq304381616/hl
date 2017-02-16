package com.hl.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 字符串工具类
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * Map值 object 转 string
     *
     * @param data
     * @return
     */
    public static Map<String, String> obj2Str(Map<String, Object> data) {
        Map<String, String> result = new HashMap<String, String>();
        for (Entry<String, Object> set : data.entrySet()) {
            result.put(set.getKey(), obj2Str(set.getValue()));
        }
        return result;
    }

    public static String obj2Str(Object data) {
        return data == null ? "" : data.toString();
    }

    /**
     * 设置指定文字，指定颜色
     *
     * @param s     原字符串
     * @param style 带颜色字符串
     * @param c
     * @param color 颜色
     * @return
     */
    public static CharSequence getColorString(String s, String style, Context c, int color) {
        if (!s.contains(style)) {
            return s;
        } else {
            int len = s.indexOf(style);
            SpannableStringBuilder builder = new SpannableStringBuilder(s);
            ForegroundColorSpan span = new ForegroundColorSpan(c.getResources().getColor(color));
            builder.setSpan(span, len, len + style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
    }

    /**
     * 将指定位置的文字变色
     *
     * @param changeText
     * @param resource
     * @param color
     * @return
     */
    @SuppressWarnings("deprecation")
    public static SpannableStringBuilder makeColorText(String changeText, String resource, Context c, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(resource);
        int start = 0;
        int index = resource.indexOf(changeText, start);
        if (index != -1) {
            style.setSpan(new ForegroundColorSpan(c.getResources().getColor(color)), index, index + changeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            while (index != -1) {
                start = index + changeText.length();
                index = resource.indexOf(changeText, start);
                if (index != -1) {
                    style.setSpan(new ForegroundColorSpan(c.getResources().getColor(color)), index, index + changeText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return style;
    }

    /**
     * @param resource
     * @Title: getNotNullValue
     * @Description: 过滤null
     * @return: String
     * @author: eye_fa
     */
    public static String getNotNullValue(String resource, String defaultValue) {
        return isEmpty(resource) ? defaultValue : resource;
    }

    /**
     * 数字字符串转成字符串集合
     *
     * @return
     */
    public static List<Integer> getListFromString(String action) {
        List<Integer> activeList = new ArrayList<Integer>();
        if (action != null) {
            String d[] = action.split(",");
            activeList = new ArrayList<Integer>();
            for (int i = 0; i < d.length; i++) {
                activeList.add(Integer.parseInt(d[i]));
            }
        }
        return activeList;
    }

    /**
     * 是否以http开头
     *
     * @param filePath 为空时返回false
     * @return
     */
    public static boolean isStartWithHttp(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            if (filePath.startsWith("http"))
                return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }
}