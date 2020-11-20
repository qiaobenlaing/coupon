package com.whhft.sysmanage.web.utils;

import java.io.UnsupportedEncodingException;

public class FusionChartUtils {
	
private static String utf8BomStr; 
	
	static{
		byte[] utf8Bom = new byte[]{(byte) 0xef, (byte) 0xbb, (byte) 0xbf};  
		try {
			utf8BomStr = new String(utf8Bom,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			utf8BomStr = "";
		}
	}	
	
	public static String xmlAdapter(String xml) {
		return utf8BomStr+xml;
	}
}
