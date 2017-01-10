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
     * 判断字符串是否为空
     *
     * @param s
     * @return 空返回 true
     */
    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || "null".equals(s);
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

}
