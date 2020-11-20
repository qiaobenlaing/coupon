package com.huift.hfq.base.utils;

import android.app.Activity;

/**
 * 有关密码的组成的设置
 * @author qian.zhou
 *
 */
public class SetPwdUtils {
	/**密码长度**/
	private static final int PWD_MINNUMBER = 6;
	private static final int PWD_MAXNUMBER = 20;
 
	public static boolean pwdform(Activity activity, String pwdcontent){
		//密码需由为6-20的字符组成
		if(pwdcontent.length() < PWD_MINNUMBER || pwdcontent.length() > PWD_MAXNUMBER){
			//Util.getContentValidate(activity, activity.getString(R.string.toast_register_between));
			return false;
		}
		return true;
	}
}
