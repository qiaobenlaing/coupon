package com.huift.hfq.base.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * 登陆
 * @author yanfang.li
 */
public class LoginUtil {
	public static <T>void login(Activity activity,Class<T> clz) {
		activity.startActivity(new Intent(activity, clz));
	}
}
