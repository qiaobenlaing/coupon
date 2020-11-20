package com.huift.hfq.cust.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 添加银行卡流程的多个Activity控制
 * @author yingchen
 *
 */
public class BankActivityUtils {
	private static List<Activity> activities = new ArrayList<Activity>();

	public static void add(Activity activity) {
		activities.add(activity);
	}

	public static void finishAll() {
		for (Activity activity : activities) {
			activity.finish();
		}
		activities.clear();
	}

	public static void remove(Activity activity) {
		for (Activity act : activities) {
			if (act == activity) {
				activities.remove(activity);
				break;
			}
		}
	}

	public void clear() {
		activities.clear();
	}
}
