package cn.suanzi.baomi.base.model;

import android.util.Log;
import cn.suanzi.baomi.base.SzApplication;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.HomeNew;
import cn.suanzi.baomi.base.pojo.HomeTab;

import com.lidroid.xutils.db.sqlite.Selector;

/**
 * 保存首页
 * @author yanfang.li
 */
public class HomeTabModel {
	private final static String TAG = HomeTabModel.class.getSimpleName();
	/** 保存home表的Id*/
	public final static String ID = "1001";
	/**
	 * 保存对象
	 */
	public static void saveHomeTab (HomeTab homeTab) {
		try {
			Log.d(TAG, "首页对象保存****************" );
			SzApplication.globalDb.saveOrUpdate(homeTab);
		} catch (Exception e) {
			Log.e(TAG, "首页对象保存>>>"+e.getMessage());
		}
	}
	
	/**
	 * 查询数据 
	 */
	public static void findAll () {
		try {
			SzApplication.globalDb.findAll(HomeTab.class);
		} catch (Exception e) {
			Log.e(TAG, "首页对象查询>>>"+e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param bid
	 * @return
	 */
	public static HomeTab getHomeTabById(String hid){
		HomeTab home = null;
		try {
			home = SzApplication.globalDb.findFirst(Selector.from(HomeTab.class).where("id", "=", hid));

		} catch (Exception e) {
			Log.e(TAG, "首页对象查询单个>>>"+e.getMessage());
		}
		
		return home;
	}
	
}
