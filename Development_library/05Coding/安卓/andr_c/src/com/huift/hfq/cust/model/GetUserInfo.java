package com.huift.hfq.cust.model;

import net.minidev.json.JSONObject;
import android.app.Activity;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.User;
import com.huift.hfq.base.pojo.UserToken;

/**
 * 获取个人信息
 * @author ad
 *
 */
public class GetUserInfo {
	/**
	 * 获取个人信息
	 */
	public static void getUserInfo(Activity activity) {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		if (null != userToken) {
			String userCode = userToken.getUserCode();
			String tokenCode = userToken.getTokenCode();
			String params[] = { userCode, tokenCode};
			new GetUserInfoTask(activity, new GetUserInfoTask.Callback() {
				
				@Override
				public void getResult(JSONObject object) {
					if (object == null) {
						return;
					} else {
						User User = Util.json2Obj(object.toString(), User.class);
						DB.saveObj(DB.Key.CUST_USER, User);
					}
					
				}
			}).execute(params);
		}
	}
}
