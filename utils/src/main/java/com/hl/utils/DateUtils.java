package com.hl.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间 格式化工具类
 */
public class DateUtils {

	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String CHINA_FORMAT = "yyyy年M月d日";

	/**
	 * 格式化日期时间
	 * 
	 * @param l
	 *            时间戳
	 * @param mode
	 *            模式
	 * @return 时间字符串
	 */
	public static String FormatDate(long l, String mode) {
		Date date = new Date(l);
		SimpleDateFormat sdf = new SimpleDateFormat(mode);
		return sdf.format(date);
	}

	/**
	 * 格式化日期时间
	 * 
	 * @param mode
	 *            模式
	 * @return 时间字符串
	 */
	public static String FormatDate(String mode) {
		return FormatDate(System.currentTimeMillis(), mode);
	}

	/**
	 * 格式化日期时间
	 * 
	 * @return 当前时间格式化后的字符串
	 */
	public static String FormatDate() {
		return FormatDate("yyyy-MM-dd HH:mm:ss");
	}

	public static String FormatDateYMDHMSS() {
		return FormatDate("yyyy-MM-dd HH:mm:ss.SSSZ");
	}

	public static String FormatDateYMD() {
		return FormatDate("yyyy年MM月dd日");
	}

	/**
	 * 获取当前年份 e.g.2015
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 
	 * @return 当前时间的17位的时间戳
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getNowTimeString() {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String curTime = sdf.format(now.getTime());
		return curTime;
	}

	// 当前时间与本地保存的时间的间隔是否大于一定时间,time 間隔時間

	public static Boolean isDisplayCheckView(Context context, int time, String times) {
		Long curTime = System.currentTimeMillis();
		if (!StringUtils.isEmpty(times)) {
			Long admiTime = Long.parseLong(times);
			if ((curTime - admiTime) < 1000 * 60 * time) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static long getTimeFromString(String time, String MODE) {
		SimpleDateFormat format = new SimpleDateFormat(MODE);
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	/**
	 * @param time
	 * @return
	 * @return String
	 */
	public static String getDateYHD(long time) {
		return getDateFormat(time, YYYY_MM_DD);
	}

	/**
	 * @param time
	 * @return
	 * @return String
	 */
	public static String getDate(long time, String MODE) {
		return getDateFormat(time, MODE);
	}

	/**
	 * @Description: 日期转换
	 * @param time
	 * @param formatType
	 * @return
	 * @return String
	 */
	@SuppressLint("SimpleDateFormat")
	private static String getDateFormat(long time, String formatType) {
		String retStr = "";
		SimpleDateFormat sf = new SimpleDateFormat(formatType);
		if (0 != time) {
			Date date = new Date(time);
			retStr = sf.format(date);
			return retStr;
		}
		return retStr;
	}

	/**
	 * 日期选择页面定制
	 * 
	 * @param str
	 * @return
	 */
	public static String getDateYDHFromStr(String str) {
		String res = "";
		res = getDateYHD(getTimeFromString(str, CHINA_FORMAT));
		return res;
	}

	public static String addOneMouth(String dt) {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add(Calendar.DATE, 1); // number of days to add
		return sdf.format(c.getTime()); // dt is now the new date
	}

	public static String formatChinaDate(String startTime) {
		String start = getDate(getTimeFromString(startTime, YYYY_MM_DD), CHINA_FORMAT).substring(5);
		LogUtils.e("DateUtils", "start===" + start);
		return start;
	}

	public static String formatChinaDate2(String startTime) {
		String start = getDate(getTimeFromString(startTime, YYYY_MM_DD), CHINA_FORMAT);
		LogUtils.e("DateUtils", "start===" + start);
		return start;
	}

}
