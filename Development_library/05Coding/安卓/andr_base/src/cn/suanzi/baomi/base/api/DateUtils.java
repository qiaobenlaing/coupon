package cn.suanzi.baomi.base.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	static final String YESTERDAY = "昨天";
	static final String TODAY = "今天";
	static final String BEFORE_YESTERDAY = "前天";
	static final String MONTH = "月";
	static final String DAY = "日";
	static final String HOUR = ":";
	static final String MINUTE = ""; // 分
	
	/**
	 * get day count between date and now
	 * @param date date count
	 */
	public static String parseDate(Date date, String formatter){

		String day;
        Date now = stringToDate(getDateString(formatter) , formatter);

        int h = date.getHours();  //  save date's hours、minutes、second information
        int m = date.getMinutes();
        int s = date.getSeconds();

        date.setHours(0);  //  set hour and minutes and second ,
        date.setMinutes(0);
        date.setSeconds(0);

		long time = (now.getTime() - date.getTime())/86400000;

        date.setHours(h);
        date.setSeconds(s);
        date.setMinutes(m);

		switch ((int)time) {
		case 0:
			day = TODAY;
			break;
		case 1:
			day = YESTERDAY;
			break;
		case 2:
			day = BEFORE_YESTERDAY;
			break;
		default:
			day = dateToString(date, "MM" + MONTH + "dd" + DAY);
			break;
		}

		return day + dateToString(date, " HH" + HOUR + "mm" + MINUTE);
	}

	/**
	 * get current date like yyyy/MM/dd HH:mm:ss
	 * @param formatter like yyyy/MM/dd HH:mm:ss
	 * @return
	 */
	public static String getDateString(String formatter) {
		SimpleDateFormat ft = new SimpleDateFormat(formatter, Locale.CHINA);
		Date dd = new Date();
		return ft.format(dd);
	}

    /**
     * get current date like yyyy/MM/dd HH:mm:ss
     * @param formatter like yyyy/MM/dd HH:mm:ss
     * @return
     */
    public static Date getDate(String formatter) {
        SimpleDateFormat ft = new SimpleDateFormat(formatter, Locale.CHINA);
        Date dd = new Date();
        return stringToDate(ft.format(dd), formatter);
    }

    /**
     * get current date
     * @return current date formatted like yyyy/MM/dd HH:mm:ss
     */
    public static Date getDate() {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        Date dd = new Date();
        return stringToDate(ft.format(dd), "yyyy/MM/dd HH:mm:ss");
    }
	
	/**
	 * convert date to string
	 * @param date
	 * @param formatter
	 * @return String
	 */
	public static String dateToString(Date date , String formatter){
		SimpleDateFormat ft = new SimpleDateFormat(formatter, Locale.CHINA);
		return ft.format(date);
	}

    /**
     * get current timesTamp , date formatted like yyyy/MM/dd HH:mm:ss
     * @return
     */
    public static Long getTimesTamp(){

        return dateToTimesTamp(getDate("yyyy/MM/dd HH:mm:ss"));
    }

    public static Long subTimeTamp(Date first, Date second){

        return dateToTimesTamp(first) - dateToTimesTamp(second);
    }

    /**
     * get current timesTamp
     * @param formatter like yyyy/MM/dd HH:mm:ss
     * @return timesTamp
     */
    public static Long getTimesTamp(String formatter){

        return dateToTimesTamp(getDate(formatter));
    }

	/**
	 * convert a date string to date 
	 * @param value value of date , like 2014/04/27 11:42:00
	 * @param format like yyyy/MM/dd HH:mm:ss 
	 * @return date
	 */
	
	public static Date stringToDate(String value , String format){
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	}
	
	/**
	 * convert a certain date to Unix timestamp
	 * @param date , like 2014-04-27 11:42:00
	 * @return Unix timestamp
	 */
	public static long dateToTimesTamp(Date date) {

		long l = date.getTime();
		String str = String.valueOf(l);
        return Long.parseLong(str.substring(0, 10));
	}

	/**
	 * convert Unix timestamp to a certain date
	 * @param timesTamp Unix timestamp
	 * @return date , like 2014-04-27 11:42:00
	 */
	public static String timesTampToDate(String timesTamp , String format) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long lcc_time = Long.valueOf(timesTamp);
		return sdf.format(new Date(lcc_time * 1000L));
	}
	
	/**
	 * system.out.println(text)
	 * @param text
	 */
	public static void Jout(String text){
		
		System.out.println(text);
	}
}