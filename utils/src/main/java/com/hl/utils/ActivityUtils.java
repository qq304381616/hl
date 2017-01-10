package com.hl.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序
 */
public class ActivityUtils {

	private static Stack<Activity> activityStack;
	private static ActivityUtils instance;

	private ActivityUtils() {
	}

	/**
	 * 单一实例
	 */
	public static ActivityUtils getInstance() {
		if (instance == null) {
			instance = new ActivityUtils();
		}
		return instance;
	}

	/**
	 * 添加Activity
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (Activity a : activityStack) {
			if (a != null) {
				a.finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * @Title: finishActivityExpet
	 * @Description: 结束除了指定Activity之外的所有Activity
	 * @param className
	 * @return: void
	 * @author: eye_fa
	 */
	public void finishActivityExpet(String className) {
		if (activityStack != null) {
			for (Activity activity : activityStack) {
				if (activity != null && !activity.getClass().getSimpleName().equals(className)) {
					activity.finish();
				}
			}
		}
	}

	/**
	 * 切换 Activity
	 */
	public static void switchActivity(Context context, Class<?> clas) {
		switchActivity(context, clas, null);
	}

	/**
	 * 切换 Activity
	 */
	public static void switchActivity(Context context, Class<?> clas, Bundle bundle) {
		Intent intent = new Intent(context, clas);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}
}