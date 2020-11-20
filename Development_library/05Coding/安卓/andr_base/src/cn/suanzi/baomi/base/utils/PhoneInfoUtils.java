package cn.suanzi.baomi.base.utils;

import android.app.Activity;
import android.telephony.TelephonyManager;

/**
 * 读取手机设备信息
 * 
 * @author yanfang.li
 */
public class PhoneInfoUtils {
	
	private final static String TAG = PhoneInfoUtils.class.getSimpleName();
	
	/** 
	 * 获取手机设备的信息
	 */
	private static TelephonyManager sTelephonyManager;

	/**
	 * 获取手机的设备号IMEI
	 * @return IMEI
	 */
	@SuppressWarnings("static-access")
	public static String getPhoneIMEI(Activity activity) {
		String IMEI = "";
		sTelephonyManager = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
		IMEI = sTelephonyManager.getDeviceId();
		return IMEI;
	}
	
}
