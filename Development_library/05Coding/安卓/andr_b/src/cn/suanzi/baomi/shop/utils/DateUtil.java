package cn.suanzi.baomi.shop.utils;

import net.minidev.json.JSONObject;
import cn.suanzi.baomi.shop.ShopConst;

public class DateUtil {
	public static final String ONE_DAY = "oneDay";
	public static final String ONE_MONTH = "oneMonth";
	public static final String ONE_YEAR = "oneYear";
	public static final String WEEK_DAY = "weekDay";
	public static final String WEEK_MONTH = "weekMonth";
	public static final String WEEK_YEAR = "weekYear";
	
	public static JSONObject setDate (int year,int month,int day) {
		JSONObject dateObj = new JSONObject();
		// 瑞年 2月29天  平年 2月28天
        // 1 3 5 7 8 10 12 31天
    	// 2 28天或者29天
    	// 4 6 9 11 30天
        int oneDay = 0;
        int oneMonth = 0;
        int oneYear = 0;
        int weekDay = 0;
        int weekMonth = 0;
        int weekYear = 0;
        if (year%400 == 0 || ((year%4 == 0) && (year%100 !=0))) { // 瑞年  2月29天
        	
    		// 31天
        	if (month == ShopConst.Month.JAN || month == ShopConst.Month.MAR 
        			|| month == ShopConst.Month.MAY || month == ShopConst.Month.JUL 
        			|| month == ShopConst.Month.AUG || month == ShopConst.Month.OCT
        			|| month == ShopConst.Month.DEC) {
        		if (day == ShopConst.Day.THIRTY_ONE) { // 31天
        			
        			if (month == ShopConst.Month.DEC && day == ShopConst.Day.THIRTY_ONE) {
                		
        				oneDay = 1;
        				oneYear = year + 1;
        				oneMonth = 1;
        				weekDay = 7;
        				weekMonth = 1;
        				weekYear = year + 1;
                	} else {
                		oneDay = 1;
        				oneYear = year;
        				oneMonth = month + 1;
        				weekDay = 7;
        				weekMonth = month + 1;
        				weekYear = year;
                	} 
        		} else if (day > ShopConst.Day.TWENTY_FOUR) { // 24号
        			oneDay = day + 1;
    				oneYear = year;
    				oneMonth = month;
    				weekDay = 7 - (31 - day);
    				weekMonth = 1;
    				weekYear = year + 1;
        		} else {
        			oneDay = day + 1;
    				oneYear = year;
    				oneMonth = month;
    				weekDay = day + 7;
    				weekMonth = month;
    				weekYear = year;
        		}
        		
        	} else if (month == ShopConst.Month.FEB) { // 29天 
        		if (day == ShopConst.Day.TWENTY_NINE) {
            		oneDay = 1;
    				oneYear = year;
    				oneMonth = month + 1;
    				weekDay = 7;
    				weekMonth = month + 1;
    				weekYear = year;
        		} else if (day > ShopConst.Day.TWENTY_TWO) { // 22号
        			oneDay = day + 1;
    				oneYear = year;
    				oneMonth = month;
    				weekDay = 7 - (29 - day);
    				weekMonth = month + 1;
    				weekYear = year;
        		} else {
        			oneDay = day + 1;
    				oneYear = year;
    				oneMonth = month;
    				weekDay = day + 7;
    				weekMonth = month;
    				weekYear = year;
        		}
        	} else if (month == ShopConst.Month.APR || month == ShopConst.Month.JUN 
        			|| month == ShopConst.Month.SEP || month == ShopConst.Month.NOV) { // 30天
        		if (day == ShopConst.Day.THIRTY) {
                		oneDay = 1;
        				oneYear = year;
        				oneMonth = month + 1;
        				weekDay = 7;
        				weekMonth = month + 1;
        				weekYear = year;
            		} else if (day > ShopConst.Day.TWENTY_THIRD) { // 23号
            			oneDay = day + 1;
        				oneYear = year;
        				oneMonth = month;
        				weekDay = 7 - (30 - day);
        				weekMonth = month + 1;
        				weekYear = year;
            		} else {
            			oneDay = day + 1;
        				oneYear = year;
        				oneMonth = month;
        				weekDay = day + 7;
        				weekMonth = month;
        				weekYear = year;
            		}
            	
        	}
        	
        	
        } else { // 平年  2月28天
        	
    		// 31天
        	if (month == ShopConst.Month.JAN || month == ShopConst.Month.MAR 
        			|| month == ShopConst.Month.MAY || month == ShopConst.Month.JUL 
        			|| month == ShopConst.Month.AUG || month == ShopConst.Month.OCT
        			|| month == ShopConst.Month.DEC) {
        		if (day == ShopConst.Day.THIRTY_ONE) {
        			
        			if (month == ShopConst.Month.DEC && day == ShopConst.Day.THIRTY_ONE) {
                		
        				oneDay = 1;
        				oneYear = year + 1;
        				oneMonth = 1;
        				weekDay = 7;
        				weekMonth = 1;
        				weekYear = year + 1;
                	} else {
                		oneDay = 1;
        				oneYear = year;
        				oneMonth = month + 1;
        				weekDay = 7;
        				weekMonth = month + 1;
        				weekYear = year;
                	} 
        		} else if (day > ShopConst.Day.TWENTY_FOUR) { // 24号
        			oneDay = day + 1;
    				oneYear = year;
    				oneMonth = month;
    				weekDay = 7 - (31 - day);
    				weekMonth = 1;
    				weekYear = year + 1;
        		} else {
        			oneDay = day + 1;
    				oneYear = year;
    				oneMonth = month;
    				weekDay = day + 7;
    				weekMonth = month;
    				weekYear = year;
        		}
        		
        	} else if (month == ShopConst.Month.FEB) { // 28天 
        		if (day == ShopConst.Day.TWENTY_EIGHT) {
            		oneDay = 1;
    				oneYear = year;
    				oneMonth = month + 1;
    				weekDay = 7;
    				weekMonth = month + 1;
    				weekYear = year;
        		} else if (day > ShopConst.Day.TWENTY_ONE) { // 21号
        			oneDay = day + 1;
    				oneYear = year;
    				oneMonth = month;
    				weekDay = 7 - (28 - day);
    				weekMonth = month + 1;
    				weekYear = year;
        		} else {
        			oneDay = day + 1;
    				oneYear = year;
    				oneMonth = month;
    				weekDay = day + 7;
    				weekMonth = month;
    				weekYear = year;
        		}
        	} else if (month == ShopConst.Month.APR || month == ShopConst.Month.JUN 
        			|| month == ShopConst.Month.SEP || month == ShopConst.Month.NOV) { // 30天
        		if (day == ShopConst.Day.THIRTY) {
                		oneDay = 1;
        				oneYear = year;
        				oneMonth = month + 1;
        				weekDay = 7;
        				weekMonth = month + 1;
        				weekYear = year;
            		} else if (day > ShopConst.Day.TWENTY_THIRD) { // 23号
            			oneDay = day + 1;
        				oneYear = year;
        				oneMonth = month;
        				weekDay = 7 - (30 - day);
        				weekMonth = month + 1;
        				weekYear = year;
            		} else {
            			oneDay = day + 1;
        				oneYear = year;
        				oneMonth = month;
        				weekDay = day + 7;
        				weekMonth = month;
        				weekYear = year;
            		}
            	
        	}
        	
        	
        }
        dateObj.put(ONE_DAY, oneDay);
        dateObj.put(ONE_MONTH, oneMonth);
        dateObj.put(ONE_YEAR, oneYear);
        dateObj.put(WEEK_DAY, weekDay);
        dateObj.put(WEEK_MONTH, weekMonth);
        dateObj.put(WEEK_YEAR, weekYear);
        return dateObj;
        
	}
}
