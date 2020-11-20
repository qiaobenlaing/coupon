package com.huift.hfq.base.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 控制多个Activity工具类
 * 
 * @author Zhonghui.Dong
 * 
 */
public class FinishActivityUtils {
	/** 定义一个activity的list集合 */
	public static List<Activity> activityList = new ArrayList<Activity>();
	/** homeFragment要退出整个应用程序 */
	public static List<Activity> homeActivityList = new ArrayList<Activity>();

	/**
	 * 向链表中添加数据
	 * 
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 向链表中添加数据
	 * @param activity
	 */
	public static void addHomeActivity(Activity activity) {
		homeActivityList.add(activity);
	}

	/**
	 * 结束当前应用程序
	 */
	public static void exit() {
		// 遍历链表
		for (Activity activity : activityList) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
		// 杀掉这个用程序释放内存
		/*
		 * int id = android.os.Process.myPid(); if (id != 0) {
		 * android.os.Process.killProcess(id); }
		 */
	}

	/**
	 * 结束掉homeActivity应用程序
	 */
	public static void exitHome() {
		// 遍历链表
		for (Activity activity : homeActivityList) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
		// 杀掉这个用程序释放内存
		/*
		 * int id = android.os.Process.myPid(); if (id != 0) {
		 * android.os.Process.killProcess(id); }
		 */
	}
}
