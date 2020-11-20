package cn.suanzi.baomi.base.model;

import android.os.Handler;

public class TheadDBhelper {
	
	/**
	 * 启动线程
	 */
	public static void runRunnable(Handler handler , Runnable runnable) {
		if (handler != null && runnable != null) {
			handler.postDelayed(runnable, 1000);
		}
	}
	
	public static void closeRunnable (Handler handler , Runnable runnable) {
		if (handler != null && runnable != null) {
			handler.removeCallbacks(runnable);
		}
	}
	
}
