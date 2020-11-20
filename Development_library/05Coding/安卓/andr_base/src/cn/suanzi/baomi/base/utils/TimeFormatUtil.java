package cn.suanzi.baomi.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取时间的工具
 * 
 * @author liyanfang
 */
public class TimeFormatUtil {

	/**
	 * 获取当前年月日的时间
	 */
	public static String getNowTimeYMD() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = dateFormat.format(new Date());
		return time;
	}

	/**
	 * 时间相加
	 * 
	 * @param time
	 * @return
	 */
	public static String addTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		try {
			cd.setTime(sdf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cd.add(Calendar.DATE, 1);// 增加一天
		Date date = cd.getTime();
		return sdf.format(date);
	}

	/**
	 * 时间相减
	 * 
	 * @param time
	 * @return
	 */
	public static String lessTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		try {
			cd.setTime(sdf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cd.add(Calendar.DATE, -1); // 减一天
		Date date = cd.getTime();
		return sdf.format(date);
	}
	
	/***
	 * 例：将2016-03-09 转化成03月9日
	 */
	public static String getMonthAndDay(String time){
		SimpleDateFormat forma = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat forma11 = new SimpleDateFormat("MM月dd日");
		Date date = null;
		try {
			date = forma.parse(time);
			System.out.println("date:"+ date.toString());
			return forma11.format(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
