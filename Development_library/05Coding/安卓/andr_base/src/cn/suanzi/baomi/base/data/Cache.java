/**
 * 
 */
package cn.suanzi.baomi.base.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cn.suanzi.baomi.base.SzApplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 缓存类。实现方式为通过DB类缓存键值，同时新建一个键值对，以EXPIRES_KEY_SUBFIX结尾，缓存过期时间。
 * 
 * @author Weiping
 */
public class Cache {

	/** 使用sharedPreference时，使用XXX_expires来缓存过期时间。*/
	public static final String EXPIRES_KEY_SUBFIX = "_expires";
	
	/** debug tag */
	public static final String TAG = "Cache";
	
	/** 单例模式 */
	protected static SharedPreferences sp = null;
	
	/**
	 * andr_base中用到的缓存键
	 */
	public interface Key {
		// public static final String CUST_USER = "Cust.User";
		/** 当前GPS地址 */
		public static final String CURR_GPS = "currGpsPosition";
	}
	
	/**
	 * 各项缓存数据的有效时间
	 * @author weiping
	 */
	public interface ValidTime {
		/** 当前GPS地址 */
		public static final long CURR_GPS = 60;
	}

	/**
	 * 获取boolean型键值。
	 * @param key 键
	 * @return 如果没有，默认返回false.
	 */
	public static boolean getBoolean(String key) {
		return getSP().getLong(key + EXPIRES_KEY_SUBFIX, 0) * 1000 < (System.currentTimeMillis()) ? 
				false : getSP().getBoolean(key, false);
	}
	
	/**
	 * 缓存整型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 * @param validSec 有效时间（秒）。键值对将在从现在开始的validSec秒之后失效。
	 */
	public static synchronized void saveBoolean(String key, boolean val, long validSec) {
		getSP().edit().putBoolean(key, val)
				.putLong(key + EXPIRES_KEY_SUBFIX, System.currentTimeMillis() / 1000 + validSec).commit();
	}

	/**
	 * 获取int型键值。
	 * @param key 键
	 * @return 过期或者没有则返回0.
	 */
	public static int getInt(String key) {
		return getSP().getLong(key + EXPIRES_KEY_SUBFIX, 0) * 1000 < (System.currentTimeMillis()) ? 
				0 : getSP().getInt(key, 0);
	}
	
	/**
	 * 缓存int型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 * @param validSec 有效时间（秒）。键值对将在从现在开始的validSec秒之后失效。
	 */
	public static synchronized void saveInt(String key, int val, long validSec) {
		getSP().edit()
				.putInt(key, val)
				.putLong(key + EXPIRES_KEY_SUBFIX, System.currentTimeMillis() / 1000 + validSec)
				.commit();
	}

	/**
	 * 获取long型键值。
	 * @param key 键
	 * @return 过期或者没有则返回0.
	 */
	public static long getLong(String key) {
		return getSP().getLong(key + EXPIRES_KEY_SUBFIX, 0) * 1000 < (System.currentTimeMillis()) ? 
				0 : getSP().getLong(key, 0);
	}
	
	/**
	 * 缓存long型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 * @param validSec 有效时间（秒）。键值对将在从现在开始的validSec秒之后失效。
	 */
	public static synchronized void saveInt(String key, long val, long validSec) {
		getSP().edit()
				.putLong(key, val)
				.putLong(key + EXPIRES_KEY_SUBFIX, System.currentTimeMillis() / 1000 + validSec)
				.commit();
	}

	/**
	 * 获取float型键值。
	 * @param key 键
	 * @return 过期或者没有则返回0.
	 */
	public static float getFloat(String key) {
		return getSP().getLong(key + EXPIRES_KEY_SUBFIX, 0) * 1000 < (System.currentTimeMillis()) ? 
				0 : getSP().getFloat(key, 0f);
	}
	
	/**
	 * 缓存float型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 * @param validSec 有效时间（秒）。键值对将在从现在开始的validSec秒之后失效。
	 */
	public static synchronized void saveFloat(String key, float val, long validSec) {
		getSP().edit()
				.putFloat(key, val)
				.putLong(key + EXPIRES_KEY_SUBFIX, System.currentTimeMillis() / 1000 + validSec)
				.commit();
	}

	/**
	 * 根据键获取值。
	 * @param key 键
	 * @return 键对应的值。如果键不存在，返回null。
	 * @throws XNDbException 
	 */
	public static String getStr(String key) {
		return getSP().getLong(key + EXPIRES_KEY_SUBFIX, 0) * 1000 < (System.currentTimeMillis()) ? 
				"": getSP().getString(key, "");
	}
	
	/**
	 * 缓存string型键值对，如果已存在则更新，否则插入。
	 * @param key 键
	 * @param val 值
	 * @param validSec 有效时间（秒）。键值对将在从现在开始的validSec秒之后失效。
	 */
	public static synchronized void saveStr(String key, String val, long validSec) {
		getSP().edit()
				.putString(key, val)
				.putLong(key + EXPIRES_KEY_SUBFIX, System.currentTimeMillis() / 1000 + validSec)
				.commit();
	}
	
	/**
	 * 根据键获取值并强制转换为对应的类型.
	 * @param key
	 * @return
	 */
	public static <T> T getObj(String key, Class<T> clz) {
		String objStr = getStr(key);
		if ("".equals(objStr)) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.fromJson(objStr, clz);
	}

	/**
	 * 缓存整个对象
	 * 
	 * @param key 键
	 * @param obj 值
	 * @param validSec 有效时间（秒）。键值对将在从现在开始的validSec秒之后失效。
	 */
	public static synchronized <T> void saveObj(String key, T obj, long validSec) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(obj);
		saveStr(key, json, validSec);
	}

	/**
	 * 删除键值对
	 */
	public static synchronized void deleteKey(String key) {
		getSP().edit()
				.remove(key)
				.remove(key + EXPIRES_KEY_SUBFIX)
				.commit();
	}
	
	/**
	 * 获取SharedPreferences
	 * @return 本地SharedPreferences
	 */
	protected static SharedPreferences getSP() {
		if (sp == null)
			sp = PreferenceManager.getDefaultSharedPreferences(SzApplication.getApplication());
		return sp;
	}
	
}
