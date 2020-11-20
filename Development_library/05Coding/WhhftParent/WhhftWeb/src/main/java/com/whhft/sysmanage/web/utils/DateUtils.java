package com.whhft.sysmanage.web.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	private static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String getCurrentTime() {
		return getCurrentTime(DEFAULT_TIME_FORMAT);
	}
	
	public static String getCurrentTime(String dateFormat) {
		DateFormat dfmt = new SimpleDateFormat(dateFormat);
		return dfmt.format(new Date());
	}
	
	public static String getTime(Date date) {
		DateFormat dfmt = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
		return dfmt.format(date);
	}
	
	public static String getTime(Date date, String dateFormat) {
		DateFormat dfmt = new SimpleDateFormat(dateFormat);
		return dfmt.format(date);
	}
	
	public static Date parse(String date) throws ParseException {
		DateFormat dfmt = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
		return dfmt.parse(date);
	}
	
	public static Date parse(String date, String dateFormat) throws ParseException {
		DateFormat dfmt = new SimpleDateFormat(dateFormat);
		return dfmt.parse(date);
	}
}
