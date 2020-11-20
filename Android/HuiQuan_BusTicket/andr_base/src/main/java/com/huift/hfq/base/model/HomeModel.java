package com.huift.hfq.base.model;

import android.util.Log;

import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.HomeNew;
import com.lidroid.xutils.db.sqlite.Selector;

/**
 * 保存首页
 * @author yanfang.li
 */
public class HomeModel {
	private final static String TAG = HomeModel.class.getSimpleName();
	/** 保存home表的Id*/
	public final static String HOME_ID = "1001";
	/**
	 * 保存对象
	 */
	public static void saveHome (HomeNew home) {
		try {
			Log.d(TAG, "首页对象保存****************" );
			SzApplication.globalDb.saveOrUpdate(home);
		} catch (Exception e) {
			Log.e(TAG, "首页对象保存>>>"+e.getMessage());
		}
	}
	
	/**
	 * 查询数据 
	 */
	public static void findAll () {
		try {
			SzApplication.globalDb.findAll(HomeNew.class);
		} catch (Exception e) {
			Log.e(TAG, "首页对象查询>>>"+e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param bid
	 * @return
	 */
	public static HomeNew getHomeById(String hid){
		HomeNew home = null;
		try {
			home = SzApplication.globalDb.findFirst(Selector.from(HomeNew.class).where("id", "=", hid));

		} catch (Exception e) {
			Log.e(TAG, "首页对象查询单个>>>"+e.getMessage());
		}
		
		return home;
	}
	
	/**
	 * 保存首页数据
	 * @param citys 城市
	 * @param homeResultStr 首页其他数据
	 */
	public static void saveHomeFragment (String citys , String homeResultStr) {
		try {
			HomeNew home = HomeModel.getHomeById(HOME_ID);
			if (null != home) {
				String city =  Util.isEmpty(home.getCitys()) ? citys : home.getCitys();
				String homeResult = Util.isEmpty(home.getHomeResultString()) ? homeResultStr : home.getHomeResultString();
				citys = Util.isEmpty(citys) ? citys = city : citys;
				homeResultStr = Util.isEmpty(homeResultStr) ? homeResultStr = homeResult : homeResultStr;
			} else {
				home = new HomeNew();
			}
			home.setId(HOME_ID);
			home.setCitys(citys);
			home.setHomeResultString(homeResultStr);
			saveHome(home);
			
		} catch (Exception e) {
			Log.e(TAG, "saveHomeFragment >>> "+e .getMessage());
		}
	}
}
