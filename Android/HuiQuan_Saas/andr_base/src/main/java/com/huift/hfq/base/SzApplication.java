package com.huift.hfq.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.huift.hfq.base.api.Tools;
import com.lidroid.xutils.DbUtils;

/**
 * Application的积累
 *
 * @author Weiping
 */
public class SzApplication extends Application {

	/** 本APP全局Application */
	private static SzApplication instance = null;
	/** 当前活动activity */
	private static Activity currActivity = null;
	/** 当前APP类型 Const.AppType，在子类(ShopApplication和CustApplication)中设置 */
	protected int currAppType = -1;
	/** 全局数据库 */
	public static DbUtils globalDb;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		try {
			globalDb = DbUtils.create(this, getFilesDir().getPath()+"/db", Const.DB_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// catch crash
		if (!Const.IS_DEBUG) {
			CrashHandler crashHandler = CrashHandler.getInstance();
			crashHandler.init(getApplicationContext());
		}
	}

	/**
	 * 获取本APP全局Application。用户创建数据库
	 */
	public static SzApplication getApplication() {
		return instance;
	}

	/**
	 * 设置应用当前活动activity
	 *
	 * @param currAct
	 *            当前活动activity的引用
	 */
	public static synchronized void setCurrActivity(Activity currAct) {
		currActivity = currAct;
	}

	/**
	 * 获取应用当前活动activity
	 *
	 * @return 当前活动activity的引用
	 */
	public static Activity getCurrActivity() {
		return currActivity;
	}

	/**
	 * @return the instance
	 */
	public static SzApplication getInstance() {
		return instance;
	}

	/**
	 * @param instance
	 *            the instance to set
	 */
	public static void setInstance(SzApplication instance) {
		SzApplication.instance = instance;
	}

	/**
	 * @return the currAppType
	 */
	public int getCurrAppType() {
		return currAppType;
	}

	/**
	 * @param currAppType
	 *            the currAppType to set
	 */
	public void setCurrAppType(int currAppType) {
		this.currAppType = currAppType;
	}

}