package com.huift.hfq.base.utils;

import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.R;
import com.huift.hfq.base.Util;

/**
 * 生成时间戳
 * 
 * @author liyanfang
 */
public class TimestampUtil {

	private static String TAG = TimestampUtil.class.getSimpleName();
	/** 随机字母数组 */
	public static String mLetter[] = { "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };

	/**
	 * 生成随机的时间戳
	 */
	public interface CallBack {
		public void getNetTime(String netTime);
	}

	public static void setRomdonValue(final Context context, final CallBack callBack) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (!Util.isNetworkOpen(context)) {
					Util.getContentValidate(R.string.login_custinternet_error);
					callBack.getNetTime(null);
				}
				// 获取网络时间
				long ld = 0;
				try {
					// URL url = new URL("http://open.baidu.com/special/time/");
					URL url = new URL(Const.ApiAddr.H5_URL + "Index/index");
					URLConnection uc = url.openConnection();// 生成连接对象
					uc.connect(); // 发出连接
					ld = uc.getDate(); // 取得网站日期时间
//					InputStream inputStream = uc.getInputStream();
//					String string = Util.inputStream2String(inputStream);
//					Log.d(TAG, "响应字符串==="+string);
					Log.d(TAG, "网络时间==" + ld);
				} catch (Exception e) {
					Log.d(TAG, "网络时间异常");
					e.printStackTrace();
				}
				if (ld == 0) {
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Util.getContentValidate(R.string.login_internet_error);
						}
					});
					return;
				}
				String hexadecimalValue = Long.toHexString(ld / 1000);
				Log.d(TAG, "hexadecimalValue = " + hexadecimalValue);
				int digit = 10 - hexadecimalValue.length(); // 位数
				if (digit > 0) {
					for (int i = 0; i < digit; i++) {
						String letterString = mLetter[(int) (Math.random() * mLetter.length)].toString();
						hexadecimalValue = Stringinsert(hexadecimalValue, letterString,
								(int) (Math.random() * hexadecimalValue.length()));
					}
				}
				Log.d(TAG, "netTime1:"+hexadecimalValue);
				callBack.getNetTime(hexadecimalValue);
			}
		}).start();

		/*
		 * if(!Util.isNetworkOpen(context)){
		 * Util.getContentValidate(R.string.login_custinternet_error); return
		 * null; }
		 * 
		 * //获取网络时间
		 * 
		 * try { URL url = new URL("http://www.bjtime.cn"); URLConnection
		 * uc=url.openConnection();//生成连接对象 uc.connect(); //发出连接 long
		 * ld=uc.getDate(); //取得网站日期时间 Log.d(TAG, "网络时间=="+ld); } catch
		 * (Exception e) { e.printStackTrace(); }
		 * 
		 * 
		 * String hexadecimalValue =
		 * Long.toHexString((Long)System.currentTimeMillis()/1000); Log.d(TAG,
		 * "hexadecimalValue = " + hexadecimalValue); int digit = 10 -
		 * hexadecimalValue.length(); // 位数 if (digit > 0) { for (int i = 0; i <
		 * digit; i++) { String letterString =
		 * mLetter[(int)(Math.random()*mLetter.length)].toString();
		 * hexadecimalValue = Stringinsert(hexadecimalValue, letterString,
		 * (int)(Math.random()*hexadecimalValue.length())); } } return
		 * hexadecimalValue;
		 */
	}

	/**
	 * 插入字符串
	 * 
	 * @param originalStr
	 *            原来字符串
	 * @param insertStr
	 *            插入字符
	 * @param index
	 *            插入的位置
	 * @return
	 */
	public static String Stringinsert(String rawStr, String insertStr, int index) {
		String beginStr = rawStr.substring(0, index);
		String endStr = rawStr.substring(index, rawStr.length());
		return beginStr + insertStr + endStr;
	}
}
