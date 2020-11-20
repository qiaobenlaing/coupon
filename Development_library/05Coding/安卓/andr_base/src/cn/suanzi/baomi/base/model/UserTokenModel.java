package cn.suanzi.baomi.base.model;

import android.util.Log;
import cn.suanzi.baomi.base.SzApplication;
import cn.suanzi.baomi.base.pojo.UserToken;

import com.lidroid.xutils.db.sqlite.Selector;

/**
 * 保存注册成功后的信息
 * @author qian.zhou
 */
public class UserTokenModel {
	private final static String TAG = UserTokenModel.class.getSimpleName();
	
	/**
	 * 保存对象
	 */
	public static void saveToken (UserToken userToken) {
		try {
			SzApplication.globalDb.saveOrUpdate(userToken);
		} catch (Exception e) {
			Log.e(TAG, "userToken对象保存>>>"+e.getMessage());
		}
	}
	
	/**
	 * 查询数据 
	 */
	public static void findAll () {
		try {
			SzApplication.globalDb.findAll(UserToken.class);
		} catch (Exception e) {
			Log.e(TAG, "userToken对象查询>>>"+e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param bid
	 * @return
	 */
	public static UserToken getHomeById(String hid){
		UserToken userToken = null;
		try {
			userToken = SzApplication.globalDb.findFirst(Selector.from(UserToken.class).where("id", "=", hid));

		} catch (Exception e) {
			Log.e(TAG, "首页对象查询单个>>>"+e);
		}
		
		return userToken;
	}
}
