package com.whhft.sysmanage.web.utils;

public class IntegerUtils {
	public static int toZeroIfNullOrEmpty(String s ){
		if( s == null || s.length() == 0) {
			return 0;
		}else{
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		
	}
}
