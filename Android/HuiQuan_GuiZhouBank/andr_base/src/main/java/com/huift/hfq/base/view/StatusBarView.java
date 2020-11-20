package com.huift.hfq.base.view;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

/**
 * 最顶端的颜色
 * @author yanfang.li
 */
public class StatusBarView {
	
		public static void statusBar(Activity activity) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        }
		}
}
