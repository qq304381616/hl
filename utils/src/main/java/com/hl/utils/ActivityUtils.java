package com.hl.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.List;
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
	 * 结束除了指定Activity之外的所有Activity
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

	/**
	 * 判断是否存在Activity
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @param className   activity全路径类名
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isActivityExists(Context context, String packageName, String className) {
		Intent intent = new Intent();
		intent.setClassName(packageName, className);
		return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
				intent.resolveActivity(context.getPackageManager()) == null ||
				context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
	}

	/**
	 * 获取launcher activity
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @return launcher activity
	 */
	public static String getLauncherActivity(Context context, String packageName) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo info : infos) {
			if (info.activityInfo.packageName.equals(packageName)) {
				return info.activityInfo.name;
			}
		}
		return "no " + packageName;
	}
}