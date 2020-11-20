package cn.suanzi.baomi.cust.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import cn.suanzi.baomi.base.Util;

/**
 * 活动中公用的方法
 * @author liyanfang
 *
 */
public class ActUtils {
  
	private static final String TAG = ActUtils.class.getSimpleName() ;    
	/** 准备第一个模板，从字符串中提取出日期数字*/    
    private static final String ORIGINAL_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss" ;    
    /** 准备第二个模板，将提取后的日期数字变为指定的格式*/    
    private static final String NOW_TIME_FORMAT = "MM.dd" ; 
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public static String formatPrice (String price) {
		String priceStr = "";
		if (Util.isEmpty(price) || Double.parseDouble(price) == 0) {
			priceStr = "0";
		} else if (price.contains(".")) {
	 		priceStr = price.substring(price.indexOf(".") + 1, price.length());
	 		if (priceStr.compareTo("0") <= 0) {
	 			priceStr = price.substring(0, price.indexOf("."));
	 		} else {
	 			priceStr = price;
	 		}
	 	} else {
	 		priceStr = price;
	 	}
		return priceStr ;
	}
	
	/**
	 * 时间转换
	 * @param startTime 要转换的时间按
	 * @return
	 */
	public static  String formatTime (String startTime) {
       if (!Util.isEmpty(startTime) && startTime.length() == ORIGINAL_TIME_FORMAT.length()) {
    	   SimpleDateFormat sdf1 = new SimpleDateFormat(ORIGINAL_TIME_FORMAT) ;        // 实例化模板对象    
           SimpleDateFormat sdf2 = new SimpleDateFormat(NOW_TIME_FORMAT) ;        // 实例化模板对象    
           Date date = null ;    
           try{    
           	date = sdf1.parse(startTime) ;   // 将给定的字符串中的日期提取出来    
           }catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理    
               Log.e(TAG, "时间格式：" + e.getMessage());       // 打印异常信息    
           }    
           
           String time = "";
	        if (sdf2.format(date).startsWith("0")) {
	        	time = sdf2.format(date).substring(1,sdf2.format(date).length());
	        } else {
	        	time = sdf2.format(date);
	        }
           return time;
       } else {
    	   return "";
       }
	}
}
