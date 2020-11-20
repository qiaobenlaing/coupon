package com.whhft.sysmanage.service.utils;

import java.text.DecimalFormat;

public class SequenceUtils {
	private static int i=1;
	private final static Long lock = new Long(5) ;
	private static DecimalFormat df = new DecimalFormat("0000");
	
	public static String get(){
		String s;
		synchronized (lock) {
			s = DateUtils.getCurrentTime("yyyyMMddHHmmss") + df.format(i++);
			if(i > 999) i =1;
		}
		return s;
	}
}
