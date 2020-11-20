package com.whhft.sysmanage.service.utils;


/**
 * 若字符串为NULL则转换为空字符串，否则返回原字符串
 * @author user
 *
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	public static String nullToEnpty(String s ){
		return s == null? "":s;
	}
	
	//根据指定的长度截取字符串，如果输入为NULL返回空字符串,如果字符串长度小于length，则返回该字符串
	public static String substring(String s, int length){
		String str = nullToEnpty( s );
		if(str.length() > length ){
			return str.substring(0, length);
		}else{
			return str;
		}
	}
}
