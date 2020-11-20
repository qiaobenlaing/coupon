package com.huift.hfq.base.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;

/**
 * 配置数据类
 * 
 * @author Weiping Liu
 * @version 1.0.0
 * @since 1.0.0
 */
public class DB {
	
	/** debug tag */
	public static final String TAG = "ConfigMdl";
	
	/** 单例模式 */
	protected static SharedPreferences sp = null;
	
	/**
	 * andr_base中用到的持久化
	 */
	public interface Key {
		/** 顾客端用户及其token信息 */
		public static final String CUST_USER_TOKEN = "Cust.UserToken";
		/** 顾客端是否第一次登录 */
		public static final String CUST_IS_FIRST_RUN = "Cust.IsFirstRun";
		/** 商店端是否第一次登录 */
		public static final String SHOP_IS_FIRST_RUN = "Shop.IsFirstRun";
		/** 商家app升级*/
		public static final String SHOP_IS_CANCEL_UPDATE = "Shop.IsCancelupdate";
		/** 商家app升级*/
		public static final String SHOP_CANCEL_UPDATE = "Shop.Cancelupdate";
		/** 客户端app升级*/
		public static final String CUST_CANCEL_UPDATE = "Cust.Cancelupdate";
		/** 保存商店信息*/
		public static final String SHOP_INFO = "shopinfo";
		/** 判断C端是否登陆*/
		public static final String CUST_LOGIN = "cust.login";
		/** C端用户信息*/
		public static final String CUST_USER = "cust.user";
		public final static String ORDER_ITEM = "orderItem";
		
	}

	/**
	 * 获取boolean型键值。
	 * @param key 键
	 * @return
	 */
	public static boolean getBoolean(String key) {
		return getSP().getBoolean(key, false);
	}
	
	/**
	 * 保存boolean型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 */
	public static synchronized void saveBoolean(String key, boolean val) {
		getSP().edit().putBoolean(key, val).commit();
	}
	
	/**
	 * 获取int型键值。
	 * @param key 键
	 * @return
	 */
	public static int getInt(String key) {
		return getSP().getInt(key, 0);
	}
	
	/**
	 * 保存int型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 */
	public static synchronized void saveInt(String key, int val) {
		getSP().edit().putInt(key, val).commit();
	}

	/**
	 * 获取long型键值。
	 * @param key 键
	 * @return
	 */
	public static long getLong(String key) {
		return getSP().getLong(key, 0);
	}
	
	/**
	 * 保存long型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 */
	public static synchronized void saveLong(String key, long val) {
		getSP().edit().putLong(key, val).commit();
	}
	
	/**
	 * 获取Float型键值。
	 * @param key 键
	 * @return
	 */
	public static float getFloat(String key) {
		return getSP().getInt(key, 0);
	}
	
	/**
	 * 保存Float型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 */
	public static synchronized void saveFloat(String key, float val) {
		getSP().edit().putFloat(key, val).commit();
	}
	
	/**
	 * 根据string型键获取值。
	 * @param key 键
	 * @return 键对应的值。如果键不存在，返回null。
	 * @throws XNDbException 
	 */
	public static String getStr(String key) {
		return getSP().getString(key, null);
	}
	
	/**
	 * 保存string型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 */
	public static synchronized void saveStr(String key, String val) {
		getSP().edit().putString(key, val).commit();
	}
	
	/**
	 * 根据键获取对象型值.
	 * @param key 键
	 * @param clz 目标类class
	 * @return 获取到的对象。如果不存在，返回null.
	 */
	public static <T> T getObj(String key, Class<T> clz) {
		String objStr = getStr(key);
		return objStr == null ? null : Util.json2Obj(objStr, clz);
	}

	/**
	 * 保存整个对象
	 * @param key 键
	 * @param obj 对象
	 */
	public static synchronized <T> void saveObj(String key, T obj) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(obj);
		saveStr(key, json);
	}

	/**
	 * 删除键值对
	 */
	public static synchronized void deleteKey(String key) {
		getSP().edit().remove(key).commit();
	}
	
	/**
	 * 获取SharedPreferences
	 * @return 本地SharedPreferences
	 */
	public static SharedPreferences getSP() {
//		return XNApplication.getApplication().getSharedPreferences(
//				XNApplication.getApplication().getPackageName(), 0);
		if (sp == null)
			sp = PreferenceManager.getDefaultSharedPreferences(SzApplication.getApplication());
		return sp;
	}
	
}
