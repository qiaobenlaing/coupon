package com.huift.hfq.shop.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.EConsuming;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActListActivity;
import com.huift.hfq.shop.activity.HomeActivity;
import com.huift.hfq.shop.activity.LoginActivity;
import com.huift.hfq.shop.utils.DialogUtils;
import com.huift.hfq.shop.utils.ExampleUtil;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.huift.hfq.shop.R;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";    
	
	
	private static final String KEY_FISRT_RUN = "firstrun";
	/**参加活动的标示*/
	private static final String JOIN_ACT = "JOIN_ACTIVITY";
	/**退出活动的标示*/
	private static final String EXIT_ACT = "EXIT_ACTIVITY";
	/**支付的标示*/
	private static final String CONSUME = "CONSUME";
	/**登录的标示*/
	private static final String LOGIN  = "LOGIN";
	
	/** 登录的jpush*/
	public static final int LOGIN_DATA = 1;
	
	private int id = 0;
	private String mTitle;
	private String mMessage;
	private Bundle mBundle;
	/**登陆的账号和密码*/
	private String mUserName;
	private String mUserPwd;
	/**登录的时间*/ 
	private String mLoginTime;
	/**支付 */
	private static int msgNum = 0;
	/**活动报名*/
	private static int joinNum = 0;
	/**活动退出*/
	private static int ExitNum = 0;
	/** 登录的推送*/
	private static int mLoginData;
	
	/**
	 * 重置消费信息数目
	 */
	public static void resetNotiNum() {
		msgNum = 0;
	}
	
	/**
	 * 重置参加活动的信息数目
	 */
	public static void resetNotiJoinNum() {
		joinNum = 0;
	}
	
	/**
	 * 重置退出活动的信息数目
	 */
	public static void resetNotiExitNum() {
		ExitNum = 0;
	}
	
	/**
	 *登录 
	 */
	public static void setInitLoginData () {
		mLoginData = 0; // 初始化数据
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
        mBundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(mBundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = mBundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            /** 判断是否为第一次使用 */
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			boolean firstRun = true;
			try {
				firstRun = sharedPreferences.getBoolean(KEY_FISRT_RUN, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Editor editor = sharedPreferences.edit();
            if (firstRun) {
            	editor.putBoolean(KEY_FISRT_RUN, false);// 修改信息
				editor.commit();
				DB.saveStr(Const.JPUSH_REGID, regId);
            }
            //发送注册ID为您的服务器...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {//接收通知
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + mBundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, mBundle);
        	mTitle = mBundle.getString(JPushInterface.EXTRA_TITLE);
        	mMessage = mBundle.getString(JPushInterface.EXTRA_MESSAGE);
        	Log.d(TAG, "messageHHHHHHHHHHHHH=" + mMessage);
            if (ShopConst.ShopMessage.INVITE_SHOP.equals(mMessage)) {//邀请入驻惠圈
            	Log.d(TAG, "INVITE_SHOP>>>>>>>" + mMessage);
            	// 自定义通知 m  
        		Notification notification = new Notification(R.drawable.ic_launcher, mTitle, System.currentTimeMillis());
        		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后自动清除图标
        		notification.defaults = Notification.DEFAULT_SOUND; // 通知默认的声音
        		String extras = mBundle.getString(JPushInterface.EXTRA_EXTRA); // 推送的附加字段
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        		int idx = ++id;
        		Intent i = new Intent(context, HomeActivity.class);
        		i.putExtras(mBundle);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            	String homeClass = DB.getStr(ShopConst.Key.HOME_MAIN); // 获取首页的类
        		String runClass = DB.getStr(ShopConst.Key.HOME); // 获取运行的类
        		Log.d(TAG, "homeClass="+homeClass+",runClass="+runClass);
        		String isHome = DB.getStr(ShopConst.Key.IS_HOME_MAIN); // 判断是否在首页运行
        		if (ShopConst.Key.IS_HOME_MAIN.equals(isHome)) {
        			joinshop (context,idx,notification,i);
        		} else {
        			if (!runClass.equals(homeClass)) {
        				joinshop (context,idx,notification,i);
        			}
        		}
         	} else if (ShopConst.ShopMessage.REMIND_SHOP.equals(mMessage)) {//添加商品展示
         		Log.d(TAG, "REMIND_SHOP>>>>>>>" + mMessage);
         	 // 自定义通知 m  
        		Notification notification = new Notification(R.drawable.ic_launcher, mTitle, System.currentTimeMillis());
        		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后自动清除图标
        		notification.defaults = Notification.DEFAULT_SOUND; // 通知默认的声音
        		String extras = mBundle.getString(JPushInterface.EXTRA_EXTRA); // 推送的附加字段
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        		int idx = ++id;
        		Intent i = new Intent(context, HomeActivity.class);
        		i.putExtras(mBundle);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            	String homeClass = DB.getStr(ShopConst.Key.HOME_MAIN); // 获取首页的类
        		String runClass = DB.getStr(ShopConst.Key.HOME); // 获取运行的类
        		Log.d(TAG, "homeClass="+homeClass+",runClass="+runClass);
        		String isHome = DB.getStr(ShopConst.Key.IS_HOME_MAIN); // 判断是否在首页运行
        		if (ShopConst.Key.IS_HOME_MAIN.equals(isHome)) {
        			addproduct (context,idx,notification,i);
        		} else {
        			if (!runClass.equals(homeClass)) {
        				addproduct (context,idx,notification,i);
        			}
        		}
            } else if (JOIN_ACT.equals(mMessage)) { // 参加活动
        		// 自定义通知
        		Notification notification = new Notification(R.drawable.ic_launcher, mTitle, System.currentTimeMillis());
        		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后自动清除图标
        		notification.defaults = Notification.DEFAULT_SOUND; // 通知默认的声音
        		String extras = mBundle.getString(JPushInterface.EXTRA_EXTRA); // 推送的附加字段
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        		int idx = ++id;
        		Intent i = new Intent(context, ActListActivity.class);
        		i.putExtras(mBundle);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        		String actClass = DB.getStr(ShopConst.ActAddKey.ACT_MAIN); // 获取活动的类
        		String runActClass = DB.getStr(ShopConst.ActAddKey.ACT); // 获取运行的类
        		Log.d(TAG, "actClass="+actClass+",runActClass="+runActClass);
        		// 判断是否在活动页运行
        		String isAct = DB.getStr(ShopConst.ActAddKey.IS_ACT_MAIN);
        		Log.d(TAG,"isAct☆☆========="+isAct);
        		if (ShopConst.ActAddKey.IS_ACT_MAIN.equals(isAct)) {
        			Log.d(TAG,"不在活动页。。。。。。。。 JOIN_ACT");
        			showAct(context,idx,notification,i);
        		} else {
        			Log.d(TAG,"在活动页。。。。。。。。 JOIN_ACT");
        			if (null != runActClass && null != actClass) {
        				if (!runActClass.equals(actClass)) {
        					Log.d(TAG,"sdasdasdasdas");
        					showAct(context,idx,notification,i);
        				}
        			}
        		}
        	} else if (EXIT_ACT.equals(mMessage)) { // 退出活动
        		// 自定义通知
        		Notification notification = new Notification(R.drawable.ic_launcher, mTitle, System.currentTimeMillis());
        		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后自动清除图标
        		notification.defaults = Notification.DEFAULT_SOUND; // 通知默认的声音
        		String extras = mBundle.getString(JPushInterface.EXTRA_EXTRA); // 推送的附加字段
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        		int idx = ++id;
        		Intent i = new Intent(context, ActListActivity.class);
        		i.putExtras(mBundle);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        		String actClass = DB.getStr(ShopConst.ActAddKey.ACT_MAIN); // 获取活动的类
        		String runActClass = DB.getStr(ShopConst.ActAddKey.ACT); // 获取运行的类
        		Log.d(TAG, "actClass="+actClass+",runActClass="+runActClass);
        		// 判断是否在活动页运行
        		String isAct = DB.getStr(ShopConst.ActAddKey.IS_ACT_MAIN);
        		Log.d(TAG,"isAct☆☆========="+isAct);
        		if (ShopConst.ActAddKey.IS_ACT_MAIN.equals(isAct)) {
        			Log.d(TAG,"不在活动页。。。。。。。。");
        			showExitAct(context,idx,notification,i);
        		} else {
        			Log.d(TAG,"在活动页。。。。。。。。");
        			if (null != actClass && null != runActClass) {
        				if (!runActClass.equals(actClass)) {
        					Log.d(TAG,"sdasdasdasdas");
        					showExitAct(context,idx,notification,i);
        				}
        			}
        		}
        	} else if (CONSUME.equals(mMessage)) {//支付
        		// 自定义通知 m  
        		Notification notification = new Notification(R.drawable.ic_launcher, mTitle, System.currentTimeMillis());
        		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后自动清除图标
        		notification.defaults = Notification.DEFAULT_SOUND; // 通知默认的声音
        		String extras = mBundle.getString(JPushInterface.EXTRA_EXTRA); // 推送的附加字段
        		EConsuming consuming = Util.json2Obj(extras, EConsuming.class);
        		if (consuming == null) {
        			return ;
        		}
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        		int idx = ++id;
        		Intent i = new Intent(context, HomeActivity.class);
        		i.putExtras(mBundle);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        		String homeClass = DB.getStr(ShopConst.Key.HOME_MAIN); // 获取首页的类
        		String runClass = DB.getStr(ShopConst.Key.HOME); // 获取运行的类
        		Log.d(TAG, "homeClass="+homeClass+",runClass="+runClass);
        		String isHome = DB.getStr(ShopConst.Key.IS_HOME_MAIN); // 判断是否在首页运行
        		if (ShopConst.Key.IS_HOME_MAIN.equals(isHome)) {
        			showNotification (context,idx,notification,i); // 是在首页运行
        		} else {
        			if (null != homeClass && null != runClass && !runClass.equals(homeClass)) {
        				showNotification (context,idx,notification,i); // 是在首页运行
        			}
        		}
        	} else if (LOGIN.equals(mMessage)) { // 登录
        		// 自定义通知
        		Notification notification = new Notification(R.drawable.ic_launcher, mTitle, System.currentTimeMillis());
        		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后自动清除图标
        		notification.defaults = Notification.DEFAULT_SOUND; // 通知默认的声音
        		String extras = mBundle.getString(JPushInterface.EXTRA_EXTRA); // 推送的附加字段
        		Log.d(TAG, "extras======="+extras);
        		try {
    				JSONObject extraJson = new JSONObject(extras);
    				if (null != extraJson && extraJson.length() > 0) {
    					mLoginTime = extraJson.getString(ShopConst.LoginKey.LOGIN_TIME);
    					Log.d(TAG, "mLoginTime========"+mLoginTime);
    				}
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        		int idx = ++id; 
        		Intent i = new Intent(context,LoginActivity.class);
        		i.putExtras(mBundle);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        		String date = mLoginTime;//登录的时间
        		String phone = "";//实际的型号   
        		showLogin();
        		mLoginData = LOGIN_DATA; // 记录数
        		HomeActivity.getJPushLoginData(mLoginData);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
        	int notifactionId = mBundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 用户点击打开了通知");//TODO
        	/*Intent i = new Intent(context, ActListActivity.class);
        	i.putExtras(bundle);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);*/
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + mBundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
        }
	}
	
	//支付
	private void showNotification (Context context,int idx,Notification notification,Intent iten) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, idx, iten, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "惠圈商户", "你有新的支付信息"+ (++msgNum)+"条", contentIntent);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(idx, notification);
	}
	
	//惠圈入驻
	private void joinshop (Context context,int idx,Notification notification,Intent i) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, idx, i, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "惠圈商户", "你有新的邀请入驻信息"+ (++msgNum)+"条", contentIntent);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(idx, notification);
	}
	//添加商品
	private void addproduct (Context context,int idx,Notification notification,Intent i) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, idx, i, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "惠圈商户", "你有新的邀请添加商品展示信息"+ (++msgNum)+"条", contentIntent);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(idx, notification);
	}
	
	//报名
	private void showAct (Context context,int idx,Notification notification,Intent i) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, idx, i, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "惠圈商户", "你有新的报名信息" + (++joinNum)+ "条", contentIntent);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(idx, notification);
	}
	
	//退出活动
	private void showExitAct (Context context,int idx,Notification notification,Intent i) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, idx, i, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "惠圈商户", "你有新的活动退出信息" + (++ExitNum)+ "条", contentIntent);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(idx, notification);
	}
	
	//重新登陆
	private void showLogin(){
		DialogUtils.show(mLoginTime,new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "bbbbbbbbbbbbbb");
				final Activity activity = AppUtils.getActivity();
				UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
				if (userToken == null) {
					Intent intent = new Intent(activity, LoginActivity.class);
					activity.startActivity(intent);
				}else{
					mUserName = userToken.getMobileNbr();
					mUserPwd = userToken.getPassword();
					Log.i(TAG, "userName==========="+mUserName);
					Log.i(TAG, "userPwd==========="+mUserPwd);
				}
				
				String registerId = null;
				//传过来的registerid
				if("".equals(registerId) && registerId == null){
					registerId = "";
				}else{
					registerId = DB.getStr(ShopConst.RegisterSave.JPUSH_REGID);
					Log.d(TAG, "jpushRegisterSave aa="+registerId);
				}
        		
				new LoginTask(activity, new LoginTask.Callback() {
					@Override
					public void getResult(int result) {
						if (result == ErrorCode.FAIL){
							Intent it = new Intent(activity, LoginActivity.class);
							activity.startActivity(it);
							activity.finish();
						}
					}
				}).execute(mUserName,mUserPwd,registerId);
			}
		});
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (ShopApplication.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(ShopApplication.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(ShopApplication.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(ShopApplication.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}
			}
			context.sendBroadcast(msgIntent);
		}
	}
}
