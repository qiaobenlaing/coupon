package com.whhft.sysmanage.web.utils;

import java.text.NumberFormat;

public class MathUtils {
	public static String doubleToPct(double d){
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(1);
		return format.format(d*100);
	}
	
	public static void main(String[] args) {
		System.out.println(MathUtils.doubleToPct(0.45678));
	}
}
