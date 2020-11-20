package com.huift.hfq.base.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class CheckNetworkUtil {

	private static final String TAG = CheckNetworkUtil.class.getSimpleName();
	/**
	 * 检测网络
	 */
	public static boolean checkNetWorkInfo(Activity activity) {
		boolean flag = false;
		ConnectivityManager conne = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 3G
		State mobile = conne.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		// wifi
		State wifi = conne.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

		NetworkInfo activeNetInfo = conne.getActiveNetworkInfo();// 获取网络的连接情况

		if (null == activeNetInfo) {
			flag = true;
		} else {
			// 获取当前的网络连接是否可用
			boolean available = activeNetInfo.isAvailable();
			if (!available) {
				flag = true;
			} else {
				if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					flag = false;
					// 判断WIFI网
				} else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
					// 判断3G网
					flag = false;
				}
			}
		}

		return flag;
	}
}
