package cn.suanzi.baomi.base.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.suanzi.baomi.base.pickadapter.AbstractWheelTextAdapter;

import android.net.ParseException;
import android.view.View;
import android.widget.TextView;

public class PickUtil {

	/** 选中的字体 */
	public static int MAXTEXTSIZE = 24;
	/** 没选中的字体 */
	public static int MINTEXTSIZE = 14;

	/**
	 * 当前的年
	 * 
	 * @return 年
	 */
	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	/**
	 * 当前的月
	 * 
	 * @return 月
	 */
	public static int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 当前的日
	 * 
	 * @return 日
	 */
	public static int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public static void setTextviewSize(String curriteItemText, AbstractWheelTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(MAXTEXTSIZE);
			} else {
				textvew.setTextSize(MINTEXTSIZE);
			}
		}
	}

	/**
	 * 返回列表索引，没有就返回默认“姓名”
	 * 
	 * @param name
	 * @return
	 */
	public static int getIndexItem(List<String> list, String name) {
		int size = list.size();
		int nameIndex = 0;
		// 输入的名字在列表里没
		boolean flagName = true;
		for (int i = 0; i < size; i++) {
			if (name.equals(list.get(i))) {
				flagName = false;
				return nameIndex;
			} else {
				nameIndex++;
			}
		}
		if (flagName) { return 0; }
		return nameIndex;
	}

	/**
	 * 时间相加
	 */
	public static String addTime (String currentTime , int addTime) {
		String time = "";
		SimpleDateFormat forma = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		Date date = null;try {
			date = forma.parse(currentTime);
			cal.setTime(date);
			cal.add(java.util.Calendar.MINUTE, addTime);
			time = forma.format(cal.getTime());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
}
