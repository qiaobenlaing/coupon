package com.huift.hfq.shop.utils;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.HomeActivity;
import com.huift.hfq.shop.activity.LoginActivity;
import com.huift.hfq.shop.model.LogoffTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.KeyEvent;
import com.huift.hfq.shop.R;

public class DialogUtils {

	private final static String TAG = "DialogUtils";

	public static Dialog dialog = null;
	/** 商家还是顾客 */
	private static final int APP_TYPE = 0;

	public static void show(String date) {
		show(date, null);
	}

	public static void show(String date, final Runnable run) {
		
		if(null != AppUtils.getActivity()){
			String context = "你的账号于";
			String lastContent = "在另一台手机登陆。如非本人操作，则密码可能泄露，建议修改密码或紧急冻结账号。";
			dialog =  new AlertDialog.Builder(AppUtils.getActivity()).setTitle(R.string.login_titleMsg).setMessage(context+date+lastContent)
			.setPositiveButton("重新登陆", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(run != null){
						run.run();
					}
					
				}
			}).setNegativeButton("退出",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					final Activity activity = AppUtils.getActivity();
					
					UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
					String tokenCode = userToken.getTokenCode();
					Log.d(TAG, "tokenCode=="+tokenCode);
					
					String registerId = null;
					//传过来的registerid
					if("".equals(registerId) && registerId == null){
						registerId = "";
					}else{
						registerId = DB.getStr(ShopConst.RegisterSave.JPUSH_REGID);
						Log.d(TAG, "jpushRegisterSave bb="+registerId);
					}
					
					new LogoffTask(activity, new LogoffTask.Callback() {
						@Override
						public void getResult(net.minidev.json.JSONObject object) {
							if(object == null){
								return ;
							}  else{
								if(String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())){
									SharedPreferences mSharedPreferences = activity.getSharedPreferences(ShopConst.LoginSave.LOGIN_KEEP, Context.MODE_PRIVATE);
					                Editor editor = mSharedPreferences.edit(); 
					                String mobileNbr = mSharedPreferences.getString("mobileNbr", "");
					    			String password = mSharedPreferences.getString("password", "");
					    			editor.clear();
					    			editor.commit();
					    			HomeActivity.setJpusLoginData();
					    			Intent it = new Intent(activity, LoginActivity.class);
									activity.startActivity(it);
									activity.finish();
								} else{
									Util.getContentValidate(R.string.app_exit_fail);
								}
							}
						}
					}).execute(tokenCode,String.valueOf(APP_TYPE),registerId);
					dialog.dismiss();
					
				}
			}).create();
			
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				   @Override
				   public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
				   {
					   if (keyCode == KeyEvent.KEYCODE_SEARCH)
					   {
						   return true;
					   }
						   
					   else{
						   
					     return false; //默认返回 false
					     
					   }
				   }
				  });
			try{
				dialog.show();
			}catch(Exception e){}
		}
		
	}
			
		
	public static void close() {
		if (dialog != null) {
			dialog.dismiss();
			//dialog = null;
		}
	}

}
