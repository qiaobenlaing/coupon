// ---------------------------------------------------------
// @author    Weiping Liu
// @version   1.0.0
// @copyright 版权所有 (c) 2014 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------

package com.huift.hfq.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.huift.hfq.base.R;

/**
 * 表单验证类
 * 
 * @author Weiping Liu
 * @version 1.0.0
 */
public class Valid {
	
	/** 待验证的表单所在的activity，用于验证失败时显示提示。 */
	private static Activity mActi;

	/**
	 * 构造函数。
	 * 
	 * @param activity
	 *            待验证的表单所在的activity，用于验证失败时显示提示。
	 */
	public Valid(Activity activity) {
		this.mActi = activity;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param num
	 *            用户输入的手机号码
	 * @return
	 */
	public static boolean phoneNo(String num) {
		boolean good = false;
		Log.i("***", "************1");
		
		if (num.length() != 11) {
			Log.i("***", "************3");
			good = true;
		} else {
			Log.i("***", "************2");
			try {
				Long l = Long.parseLong(num);
			} catch (Exception e) {
				good = true;
			}
		}
		if (good) {
			Util.addToast(R.string.hint_right_phone);
		}
		return good;
	}


	/**
	 * 验证密码设置是否符合要求
	 * @param pwd 密码
	 * @param cfmPwd 确认密码
	 * @return
	 */
	public static boolean isCorrPwd(String pwd, String cfmPwd) {
		boolean result = false;

		if (!isNotEmptyStr(pwd, "密码") || !isNotEmptyStr(cfmPwd, "确认密码")) {
//			Toast.makeText(acti, "别着急！您的信息还没填完整！请输入密码", Toast.LENGTH_SHORT).show();
		} else if (!pwd.equals(cfmPwd)) {
			Toast.makeText(mActi, "两次密码不一致！", Toast.LENGTH_SHORT).show();
		} else if (pwd.length() < 4) {
			Toast.makeText(mActi, "密码长度不少于4位", Toast.LENGTH_SHORT).show();
		} else {
			result = true;
		}

		return result;

	}

	/**
	 * 不为空
	 * 
	 * @param s 待验证的字符串
	 * @param name 要显示在消息中的名字
	 * @return
	 */
	public static boolean isNotEmptyStr(String s, String name) {
		boolean good = isNotEmptyStr(s);
		if (!good) {
			Toast.makeText(mActi, "别着急！您的信息还没填完整！请输入" + name, Toast.LENGTH_SHORT).show();
		}
		return good;
	}

	/**
	 * 不为空,不需要提示
	 * 
	 * @author sheng.xu
	 * @param s
	 *            待验证的字符串
	 * @return
	 */
	public static boolean isNotEmptyStr(String s) {
		return s != null && s.length() > 0;
	}

	/**
	 * 将空指针String转化为""
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return
	 */
	public static String getNonNullString(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 
	 * @author Chenghao.Feng 身份证验证 功能：判断身份证号是否正确
	 * @param String
	 *            待检查身份证号
	 * @return boolean
	 */

	@SuppressLint("SimpleDateFormat")
	public static boolean isValidIDCard(String IDStr) {
		String numOfId = "";
		IDStr = IDStr.toLowerCase();

		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 18 && IDStr.length() != 19) {
			return false;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			numOfId = IDStr.substring(0, 17);
		} else if (IDStr.length() == 19) {
			numOfId = IDStr.substring(0, 18);
		}
		if (isNumeric(numOfId) == false) {
			return false;
		}
		// =======================(end)========================

		// // ================ 出生年月是否有效 ================
		// /* isDate() 方法可能有错 */
		// String strYear = Ai.substring(6, 10);// 年份
		// String strMonth = Ai.substring(10, 12);// 月份
		// String strDay = Ai.substring(12, 14);// 月份
		// if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
		// Log.e(TAG, "身份证生日无效。");
		// return false;
		// }
		// GregorianCalendar gc = new GregorianCalendar();
		// SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		// try {
		// if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
		// || (gc.getTime().getTime() - s.parse(
		// strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
		// Log.e(TAG, "身份证生日不在有效范围。");
		// return false;
		// }
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// } catch (java.text.ParseException e) {
		// e.printStackTrace();
		// }
		// if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) ==
		// 0) {
		// Log.e(TAG, "身份证月份无效。");
		// return false;
		// }
		// if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
		// Log.e(TAG, "身份证日期无效。");
		// return false;
		// }
		// Log.i(TAG, "身份证出生年月正确。");
		// // =====================(end)=====================

//		// ================ 地区码时候有效 ================
//		Hashtable<?, ?> h = GetAreaCode();
//		if (h.get(Ai.substring(0, 2)) == null) {
//			Log.e(TAG, "身份证地区编码错误。");
//			return false;
//		}
//		Log.i(TAG, "地区码验证正确。");
//		// ==============================================

//		// ================ 判断最后一位的值 ================
//		int TotalmulAiWi = 0;
//		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
//		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
//		for (int i = 0; i < 17; i++) {
//			TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
//		}
//		int modValue = TotalmulAiWi % 11;
//		String strVerifyCode = ValCodeArr[modValue];
//		Ai = Ai + strVerifyCode;
//
//		if (IDStr.length() == 18) {
//			if (Ai.equals(IDStr) == false) {
//				Log.e(TAG, "Ai=" + Ai + "IDStr=" + IDStr);
//				Log.e(TAG, "最后一位的值出错。");
//				return false;
//			}
//		}
//		Log.i(TAG, "最后一位的值正确。");
//
//		// =====================(end)=====================

		return true;
	}

//	/**
//	 * 功能：设置地区编码
//	 * 
//	 * @return Hashtable 对象
//	 */
//	private static Hashtable<String, String> GetAreaCode() {
//		Hashtable<String, String> hashtable = new Hashtable<String, String>();
//		hashtable.put("11", "北京");
//		hashtable.put("12", "天津");
//		hashtable.put("13", "河北");
//		hashtable.put("14", "山西");
//		hashtable.put("15", "内蒙古");
//		hashtable.put("21", "辽宁");
//		hashtable.put("22", "吉林");
//		hashtable.put("23", "黑龙江");
//		hashtable.put("31", "上海");
//		hashtable.put("32", "江苏");
//		hashtable.put("33", "浙江");
//		hashtable.put("34", "安徽");
//		hashtable.put("35", "福建");
//		hashtable.put("36", "江西");
//		hashtable.put("37", "山东");
//		hashtable.put("41", "河南");
//		hashtable.put("42", "湖北");
//		hashtable.put("43", "湖南");
//		hashtable.put("44", "广东");
//		hashtable.put("45", "广西");
//		hashtable.put("46", "海南");
//		hashtable.put("50", "重庆");
//		hashtable.put("51", "四川");
//		hashtable.put("52", "贵州");
//		hashtable.put("53", "云南");
//		hashtable.put("54", "西藏");
//		hashtable.put("61", "陕西");
//		hashtable.put("62", "甘肃");
//		hashtable.put("63", "青海");
//		hashtable.put("64", "宁夏");
//		hashtable.put("65", "新疆");
//		hashtable.put("71", "台湾");
//		hashtable.put("81", "香港");
//		hashtable.put("82", "澳门");
//		hashtable.put("91", "国外");
//		return hashtable;
//	}

	/**
	 * 
	 * @author Chenghao Feng 功能：判断字符串是否为数字
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	// /**
	// * 功能：判断字符串是否为日期格式
	// *
	// * @param str
	// * @return
	// */
	// public static boolean isDate(String strDate) {
	// Pattern pattern = Pattern
	// .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?{1}quot;");
	// Matcher m = pattern.matcher(strDate);
	// if (m.matches()) {
	// return true;
	// } else {
	// return false;
	// }
	// }

}
