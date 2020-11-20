package com.huift.hfq.base.utils;

import android.util.Log;

import com.huift.hfq.base.SzApplication;
import com.lidroid.xutils.exception.DbException;

public class DBUtils {

	private static final String TAG = DBUtils.class.getSimpleName();
	
	/**
	 * 保存对象
	 * @param t 对象
	 */
	public static <T> void findAll(Class<T> t) {
		try {
			SzApplication.globalDb.findAll(t);
		} catch (DbException e) {
			Log.e(TAG, "数据库=" + e.getMessage(), e); 
		}
	}
	
	/**
	 * 保存对象
	 * @param t 对象
	 */
	public static <T> void save(T t) {
		try {
			SzApplication.globalDb.save(t);
		} catch (DbException e) {
			Log.e(TAG, "数据库=" + e.getMessage(), e); 
		}
	}
	
	/**
	 * 保存对象或者修改
	 * @param t 对象
	 */
	public static <T> void saveOrUpdate(T t) {
		try {
			SzApplication.globalDb.saveOrUpdate(t);
		} catch (DbException e) {
			Log.e(TAG, "数据库=" + e.getMessage(), e); 
		}
	}

	/**
	 * 修改对象
	 * @param t
	 */
	public static <T> void update(T t) {
		try {
			SzApplication.globalDb.update(t);
		} catch (DbException e) {
			Log.e(TAG, "数据库=" + e.getMessage(), e); 
		}		
	}



}
