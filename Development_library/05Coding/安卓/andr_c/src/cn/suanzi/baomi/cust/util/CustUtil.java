package cn.suanzi.baomi.cust.util;

import android.content.Context;

public class CustUtil {
	/**
	 * 根据文件名称 获取资源id
	 * name ---资源名称
	 * file ---文件夹名称
	 * packageName---包名
	 */
	public static int getResourceId(Context context,String name,String file,String packageName){
		int id = context.getResources().getIdentifier(name, file, packageName); 
		return id;
	}
	
	/**
	 * 获取控件 id
	 * @param context
	 * @param name 控件名称
	 * @param packageName  包名
	 * @return
	 */
	public static int getWidgetId(Context context,String name,String packageName){
		return getResourceId(context, name, "id", packageName);
	}
}
