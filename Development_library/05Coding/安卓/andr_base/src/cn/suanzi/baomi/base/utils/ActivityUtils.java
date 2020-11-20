package cn.suanzi.baomi.base.utils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;

/**
 * 控制多个Activity工具类
 * 
 * @author Zhonghui.Dong
 * 
 */
@SuppressLint("NewApi")
public class ActivityUtils {
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
