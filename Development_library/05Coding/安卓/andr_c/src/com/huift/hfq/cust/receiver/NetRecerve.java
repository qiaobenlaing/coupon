package com.huift.hfq.cust.receiver;

import com.huift.hfq.cust.activity.HomeActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络监听
 * @author ad
 *
 */
public class NetRecerve extends BroadcastReceiver {

	private final static String TAG = NetRecerve.class.getSimpleName();
	private HomeActivity mHomeActivity;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			// 获得系统网络连接管理服务
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获得网络连接信息
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
				Log.i(TAG, "网络连接正常");
				if (null != mHomeActivity) {
					mHomeActivity.netRecerve(true);
				}
			} else {
				Log.i(TAG, "无连接");
				if (null != mHomeActivity) {
					mHomeActivity.netRecerve(false);
				}
			}
		}
	}
	
	/**
	 * 设置你哟啊监听的界面
	 * @param homeFragment
	 */
	public void setHomeFragment(HomeActivity homeActivity) {
		this.mHomeActivity = homeActivity;
	}

	/**
	 * 打开系统网络设置
	 * @param context
	 */
	public static void openSettingNet(Context context) {
		Intent intent = null;
		// 判断手机系统的版本 即API大于10 就是3.0或以上版
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
			Log.d(TAG, "api level 10");
		} else {
			intent = new Intent();
			ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
			Log.d(TAG, "api level less 10");
		}
		context.startActivity(intent);
	}

}
