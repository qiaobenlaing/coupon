package com.huift.hfq.base.model;

import android.util.Log;

import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.pojo.GuideImg;
import com.huift.hfq.base.pojo.GuideImgb;
import com.lidroid.xutils.db.sqlite.Selector;

/**
 * 保存首页
 * @author yanfang.li
 */
public class GuideImgModel {
	private final static String TAG = GuideImgModel.class.getSimpleName();
	/** 保存home表的Id*/
	public final static String ID = "1001";
	/**
	 * 保存对象(顾客端)
	 */
	public static void saveGuideImg (GuideImg guideImg) {
		try {
			Log.d(TAG, "首页对象保存****************" );
			SzApplication.globalDb.saveOrUpdate(guideImg);
		} catch (Exception e) {
			Log.e(TAG, "首页对象保存>>>"+e.getMessage());
		}
	}
	
	/**
	 * 保存对象(商家端)
	 */
	public static void saveGuideImg_b (GuideImgb guideImgb) {
		try {
			Log.d(TAG, "首页对象保存****************" );
			SzApplication.globalDb.saveOrUpdate(guideImgb);
		} catch (Exception e) {
			Log.e(TAG, "首页对象保存>>>"+e.getMessage());
		}
	}
	
	/**
	 * 查询数据 
	 */
	public static void findAll () {
		try {
			SzApplication.globalDb.findAll(GuideImg.class);
		} catch (Exception e) {
			Log.e(TAG, "首页对象查询>>>"+e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param bid(查询顾客端)
	 * @return
	 */
	public static GuideImg getGuideById(String hid){
		GuideImg guideImg = null;
		try {
			guideImg = SzApplication.globalDb.findFirst(Selector.from(GuideImg.class).where("id", "=", hid));
		} catch (Exception e) {
			Log.e(TAG, "首页对象查询单个>>>"+e.getMessage());
		}
		
		return guideImg;
	}
	
	/**
	 * 
	 * @param bid(查询商家端)
	 * @return
	 */
	public static GuideImgb getGuidebById(String hid){
		GuideImgb guideImgb = null;
		try {
			guideImgb = SzApplication.globalDb.findFirst(Selector.from(GuideImgb.class).where("id", "=", hid));
		} catch (Exception e) {
			Log.e(TAG, "首页对象查询单个>>>"+e.getMessage());
		}
		
		return guideImgb;
	}
	
}
