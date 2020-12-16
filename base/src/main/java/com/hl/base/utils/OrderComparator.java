package com.hl.base.utils;

import com.hl.base.entity.BasePinyin;

import java.util.Comparator;

/**
 * 按 order 字段排序。
 * 为空项目放到后面。
 * 如果相同，按拼音排序。第一个字符 第二个字符 开始比较。。
 */
public class OrderComparator implements Comparator<BasePinyin> {

    @Override
    public int compare(BasePinyin o1, BasePinyin o2) {
        if (o1.getOrderNo() != null && o2.getOrderNo() == null) {
            return -1;
        } else if (o1.getOrderNo() == null && o2.getOrderNo() != null) {
            return 1;
        } else if (o1.getOrderNo() != null && o2.getOrderNo() != null && !o1.getOrderNo().equals(o2.getOrderNo())) {
            return o1.getOrderNo() > o2.getOrderNo() ? -1 : 1;
        } else {
            int i = 0;
            String w1 = o1.getWhole();
            String w2 = o2.getWhole();
            do {
                if (w1.length() <= i) {
                    return -1;
                } else if (w2.length() <= i) {
                    return 1;
                }
                char c1 = w1.charAt(i);
                char c2 = w2.charAt(i);
                if (c1 == c2) {
                    i++;
                    continue;
                }
                return c1 > c2 ? 1 : -1;
            }
            while (true);
        }
    }
}
