package cn.suanzi.baomi.base.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

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
		Log.d(TAG, "net is >>> " + mobile.toString());
		// wifi
		State wifi = conne.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		Log.d(TAG, "net is >>> " + wifi.toString());

		NetworkInfo activeNetInfo = conne.getActiveNetworkInfo();// 获取网络的连接情况
		Log.d(TAG, "I come back");
		
		if (null == activeNetInfo) {
			flag = true;
		} else {
			// 获取当前的网络连接是否可用
			boolean available = activeNetInfo.isAvailable();
			if (!available) {
				Log.i("通知", "当前的网络连接可用>>>>>>");
				flag = true;
			} else {
				Log.i("通知", "当前的网络连接可用");
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
