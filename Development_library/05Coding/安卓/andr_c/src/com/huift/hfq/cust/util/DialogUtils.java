package com.huift.hfq.cust.util;

import com.huift.hfq.cust.activity.HomeActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.LogoffTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.PopupWindow;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.PopupWindowUtils;
import com.huift.hfq.base.utils.PopupWindowUtils.OnResultListener;
import com.huift.hfq.cust.R;

/**
 * 多方登陆的对话框
 * @author wensi.yu
 *
 */
public class DialogUtils {
	
	private final static String TAG = "DialogUtils";
	
	/**登陆的账号和密码*/
	private static String userName;
	private static String userPwd;
	/**商家还是顾客*/
	private static final int APP_TYPE = 1;
	public static PopupWindow showLoginDialog;
	
	public static void show(String date){
		show(date,null);
	}
	
	public static void show(String date,final Runnable run){
		if(null != AppUtils.getActivity()){
			String title = "下线通知";
			String content = "你的账号于";
			String lastContent = "在另一台手机登录。如非本人操作，则密码可能泄露，建议修改密码或紧急冻结账号。";
			Log.d(TAG, "AppUtils.getActivity()="+AppUtils.getActivity());
			
			showLoginDialog = PopupWindowUtils.showLoginDialog(AppUtils.getActivity(), title, content+date+lastContent, "重新登录", "退出", new OnResultListener() {
				@Override
				public void onOK() {
					if(run != null){
						run.run();
					}
				}
				
				@Override
				public void onCancel() {
					final Activity activity = AppUtils.getActivity(); //当前activity
					UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
					String tokenCode = userToken.getTokenCode();
					Log.d(TAG, "tokenCode=="+tokenCode);
					
					//传过来的registerid
					String regId = null;
					if("".equals(regId) && regId == null){
						regId = "";
					}else{
						regId = DB.getStr(CustConst.JPush.JPUSH_REGID);
						Log.d(TAG,"RegisterSave aa="+regId);
					}
					
					new LogoffTask(activity, new LogoffTask.Callback() {
						@Override
						public void getResult(net.minidev.json.JSONObject object) {
							if(object == null){
								return ;
							}  else{
								if(String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())){
									SharedPreferences mSharedPreferences = activity.getSharedPreferences(CustConst.LoginSave.LOGIN_KEEP, Context.MODE_PRIVATE);
									Editor editor = mSharedPreferences.edit();
									editor.clear();
									editor.commit();
									DB.saveBoolean(DB.Key.CUST_LOGIN, false);
									HomeActivity.setJpusLoginData();
					    			Intent it = new Intent(activity, LoginActivity.class);
					    			it.putExtra(LoginTask.ALL_LOGIN, Const.Login.EXIT_LOGIN);
					    			activity.startActivity(it);
					    			activity.finish();
								} else{
									Util.getContentValidate(R.string.app_exit_fail);
								}
							}
						}
					}).execute(tokenCode, String.valueOf(APP_TYPE),regId);
				}
				
			});
		}	
	}
	
	public static void close(Activity activity){
		if(null != activity && null != showLoginDialog){
			showLoginDialog.dismiss();
		}
	}
}
