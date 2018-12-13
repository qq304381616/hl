package com.hl.base.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * pinyin4j 汉语拼音工具类
 */
public class Pinyin {

    private HanyuPinyinOutputFormat format;

    private boolean simple;

    public Pinyin setSimple(boolean simple) {
        this.simple = simple;
        return this;
    }

    public Pinyin() {
        this(1);
    }

    public Pinyin(int type) {
        format = new HanyuPinyinOutputFormat();
        switch (type) {
            case 1: // 默认无声调
                format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 控制声调 无/数字/符号
                break;
            case 2: // 上方显示声调
                format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
                format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
                break;
            default:
                format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
                break;
        }
    }

    // 获取拼音第一个字母
    private String getCharPinyin(char c) {
        String[] pinyin = null; // 不是汉字会返回 null
        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);

        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return pinyin == null ? null : pinyin[0]; // 如果是多音字，仅取第一个发音
    }

    /**
     * 转换一个字符串
     */
    public String getPinYin(String str) {
        if (str == null || "".equals(str)) return str;
        StringBuilder sb = new StringBuilder();
        String tempPinyin;
        for (int i = 0; i < str.length(); ++i) {
            tempPinyin = getCharPinyin(str.charAt(i));
            if (tempPinyin == null) {
                sb.append(str.charAt(i)); // 如果str.charAt(i)非汉字，则保持原样
            } else {
                if (simple) sb.append(tempPinyin.substring(0, 1));
                else sb.append(tempPinyin);
            }
        }
        return sb.toString();
    }
}
