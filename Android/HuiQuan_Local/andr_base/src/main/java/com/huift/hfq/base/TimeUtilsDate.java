package com.huift.hfq.base;

import java.sql.Timestamp;

public class TimeUtilsDate {
	
	public static String getStringTime(long now) {
		return android.text.format.DateFormat.format("yyyy年MM月dd日 kk:mm", now).toString();
	}
	
	public static long getNow() {
		String now=android.text.format.DateFormat.format("yyyyMMddkkmmss", System.currentTimeMillis()).toString();
		return Long.parseLong(now);
	}
	
	public static Integer getNowYear() {
		String now=android.text.format.DateFormat.format("yyyy", System.currentTimeMillis()).toString();
		return Integer.parseInt(now);
	}
	
	public static String beforeMinutes(int m) {
		long time = System.currentTimeMillis()-m*60000;
		String now=android.text.format.DateFormat.format("yyyyMMddkkmmss", time).toString();
		return now;
	}
	
	public static String getNowString() {
		String now=android.text.format.DateFormat.format("yyyyMMddkkmmss", System.currentTimeMillis()).toString();
		return now;
	}
	
	public static String getNowString(String format) {
		String now=android.text.format.DateFormat.format(format, System.currentTimeMillis()).toString();
		return now;
	}

	public static String format(String format,long time) {
		String now=android.text.format.DateFormat.format(format, time).toString();
		return now;
	}

	public static String format(String format,Timestamp time) {
		if(time==null){
			return null;
		}
		String now=android.text.format.DateFormat.format(format, time).toString();
		return now;
	}

}
