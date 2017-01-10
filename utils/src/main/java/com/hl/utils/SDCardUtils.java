package com.hl.utils;

/**
 * sdcard 工具类
 */
public class SDCardUtils {

	/**
	 * 判断SD卡是否存在
	 */
	public static boolean isExistSDCard() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取 sdcard 路径
	 */
	public static String getSDCardPath() {
		return isExistSDCard() ? android.os.Environment.getExternalStorageDirectory().getAbsolutePath() : null;

	}
}
