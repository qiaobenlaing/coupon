package cn.suanzi.baomi.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.EditText;
import cn.suanzi.baomi.base.R;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.BatchCoupon;

/**
 * 时间
 * 
 * @author yanfang.li
 * 
 */
public class TimeUtils {

	private static final String TAG = TimeUtils.class.getSimpleName();

	/**
	 * 使用日期
	 * 
	 * @param activity
	 * @param batchCoupon
	 * @return
	 */
	public static String getTime(BatchCoupon batchCoupon) {
		String useDate = "";
		String startDate = Util.getString(R.string.use_limit_date);
		String startTime = batchCoupon.getStartUsingTime().replace("-", ".");
		String endTime = batchCoupon.getExpireTime().replace("-", ".");

		if ((Util.isEmpty(startTime) && Util.isEmpty(endTime)) || (startTime.equals(startDate) && endTime.equals(startDate))) {
			useDate = Util.getString(R.string.no_limit_time);
		} else {
			// 开始时间
			if (Util.isEmpty(startTime)) {
				useDate = "";
			} else {
				useDate = startTime;
			}
			// 结束时间
			if (Util.isEmpty(endTime)) {
				if (Util.isEmpty(useDate)) {
					useDate = "";
				} else {
					useDate = Util.getString(R.string.start_use_time) + startTime;
				}
			} else {
				if (Util.isEmpty(useDate)) {
					useDate = Util.getString(R.string.end_use_time) + useDate;
				} else {
					useDate = useDate + " - " + endTime;
				}
			}
		}
		return useDate;
	}

	/**
	 * 使用日期
	 * 
	 * @param activity
	 * @param batchCoupon
	 * @return
	 */
	public static String getBTime(BatchCoupon batchCoupon) {
		String useDate = "";
		String startDate = Util.getString(R.string.use_limit_date);
		if ((Util.isEmpty(batchCoupon.getStartUsingTime()) && Util.isEmpty(batchCoupon.getExpireTime()))
				|| (batchCoupon.getDayStartUsingTime().equals(startDate) && batchCoupon.getDayEndUsingTime().equals(
						startDate))) {
			useDate = Util.getString(R.string.use_time) + Util.getString(R.string.no_limit_time);
		} else {
			// 开始时间
			if (Util.isEmpty(batchCoupon.getStartUsingTime())) {
				useDate = "";
			} else {
				useDate = batchCoupon.getStartUsingTime();
			}
			// 结束时间
			if (Util.isEmpty(batchCoupon.getExpireTime())) {
				if (Util.isEmpty(useDate)) {
					useDate = "";
				} else {
					useDate = Util.getString(R.string.start_use_time)
							+ batchCoupon.getStartUsingTime();
				}
			} else {
				if (Util.isEmpty(useDate)) {
					useDate = Util.getString(R.string.end_use_time) + batchCoupon.getExpireTime();
				} else {
					useDate = Util.getString(R.string.use_time) + useDate + " - " + batchCoupon.getExpireTime();
				}
			}
		}
		return useDate;
	}

	/**
	 * 每天使用时间
	 * 
	 * @param activity
	 * @param batchCoupon
	 * @return
	 */
	public static String getDate( BatchCoupon batchCoupon) {

		String useDate = "";
		String startDate = Util.getString(R.string.start_day_date);
		String endDate = Util.getString(R.string.end_day_date);
		if ((Util.isEmpty(batchCoupon.getDayStartUsingTime()) && Util.isEmpty(batchCoupon.getDayEndUsingTime()))
				|| (batchCoupon.getDayStartUsingTime().equals(startDate) && batchCoupon.getDayEndUsingTime().equals(
						endDate))) {
			useDate = Util.getString(R.string.day_use);
		} else {
			// 开始时间
			if (Util.isEmpty(batchCoupon.getDayStartUsingTime())) {
				useDate = "";
			} else {
				useDate = batchCoupon.getDayStartUsingTime();
			}
			// 结束时间
			if (Util.isEmpty(batchCoupon.getDayEndUsingTime())) {
				if (Util.isEmpty(useDate)) {
					useDate = Util.getString(R.string.day_use);
				} else {
					useDate = batchCoupon.getDayStartUsingTime();
				}
			} else {
				if (Util.isEmpty(useDate)) {
					useDate = "" + batchCoupon.getDayEndUsingTime();
				} else {
					useDate = useDate + " - " + batchCoupon.getDayEndUsingTime();
				}
			}
		}
		return useDate;
	}

	/**
	 * 当前时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate(String dateStyle) {
		String dataTime = "";
		SimpleDateFormat format = new SimpleDateFormat(dateStyle);
		dataTime = format.format(new Date());
		return dataTime;
	}

	/**
	 * 优惠券可使用时间
	 * 
	 * @param coupon
	 *            优惠券对象
	 * @return true 可以使用 false 不可以使用
	 */
	public static boolean getCanCoupon(BatchCoupon coupon) {
		boolean flag = false;
		String date = getCurrentDate("yyyy.MM.dd"); // 今天的年月日
		String dateTime = getCurrentDate("HH:mm"); // 现在的时间
		int startCalDate = 0;
		int endCalDate = 0;
		int dayStartCalDate = 0;
		int dayEndCalDate = 0;
		// 判断使用开始日期是否为空 空就是长期有效
		if (!Util.isEmpty(coupon.getStartUsingTime())) {
			startCalDate = date.compareTo(coupon.getStartUsingTime());
		}
		if (!Util.isEmpty(coupon.getExpireTime())) {
			endCalDate = coupon.getExpireTime().compareTo(date);
		}
		if (!Util.isEmpty(coupon.getDayStartUsingTime())) {
			dayStartCalDate = dateTime.compareTo(coupon.getDayStartUsingTime());
		}
		if (!Util.isEmpty(coupon.getDayEndUsingTime())) {
			dayEndCalDate = coupon.getDayEndUsingTime().compareTo(dateTime);
		}
		// 判断时间和今天的时间是否有效
		if (startCalDate >= 0 && endCalDate >= 0 && dayStartCalDate >= 0 && dayEndCalDate >= 0) {
			Log.d(TAG, "使用OK");
			flag = true;
		} else {
			Log.d(TAG, "待使用NO");
			if (startCalDate < 0) {
				// 未到使用时间 
				Util.getContentValidate(R.string.use_no_time);
			} else if ((startCalDate > 0 && endCalDate < 0)
					|| (startCalDate > 0 && endCalDate == 0 && dayStartCalDate > 0 && dayEndCalDate < 0)) {
				// 已过期
				Util.getContentValidate(R.string.use_time_fail);

			} else {
				// 没有过期但是每天使用时间不在使用范围内
				if (dayStartCalDate < 0) {
					// 商店还没开门
					Util.getContentValidate(R.string.use_nocsm_time);
				} else if (dayStartCalDate > 0 && dayEndCalDate < 0) {
					// 商店关门了
					Util.getContentValidate(R.string.use_nocsm_time);
				}
			}

			flag = false;
		}
		return flag;
	}
	
	/**
	 * 判断店铺的营业时间
	 * @param activity
	 * @param shop
	 * @return
	 */
	public static boolean getShopTime (EditText etstartTime, EditText etendTime){
		  boolean flag = false;
		  String startHour = etstartTime.getText().toString().substring(0, 2);
		  String startMinute = etstartTime.getText().toString().substring(3, etstartTime.getText().toString().length());
		  String endHour = etendTime.getText().toString().substring(0, 2);
		  String endMinute = etendTime.getText().toString().substring(3, etendTime.getText().toString().length());
		  int statrTime = (Integer.parseInt(startHour) *60) + Integer.parseInt(startMinute);
		  int endsTime = (Integer.parseInt(endHour) *60) + Integer.parseInt(endMinute);
		  Log.d(TAG, "startHour>>>" + startHour + "startMinute>>>" + startMinute);
		  Log.d(TAG, "endHour>>>" + endHour + "endMinute>>>" + endMinute);
		  Log.d(TAG, "statrTime>>>" + statrTime);
		  Log.d(TAG, "endTime>>>" + endsTime);
		  if(statrTime < endsTime){
			  flag = true;
			  Log.d(TAG, "commim......");
		  } else {
			  flag = false;
			  Log.d(TAG, "commim...no.....");
		  }
		  return flag;
	}
	
	/**
	 * 计算返回的距离
	 * @param distanceStr 输入的距离
	 * @return 返回int类型的距离
	 */
	public static String getDistance (String distanceStr) {
		String distatnceSimple = "";
		if (Util.isEmpty(distanceStr)) {
			// 为空 不处理
			distatnceSimple = "0 M";
		} else {
			String distanceSrc = distanceStr.replace(",", "").replace(".", "");
			try {
				int distance = Integer.parseInt(distanceSrc);
				if (distance > 1000) {
					int dist = distance / 1000;
					if (dist > 100) {
						distatnceSimple = ">100 Km";
					} else {
						distatnceSimple = String.valueOf(dist) + " Km";
					}
				} else {
					distatnceSimple = String.valueOf(distance) + " M";
				}
			} catch (Exception e) {
				Log.e(TAG, "距离计算出错  >>> " + e.getMessage());
			}

		}
		return distatnceSimple;
	}
}
