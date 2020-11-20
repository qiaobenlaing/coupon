
package cn.suanzi.baomi.cust.receiver;

import org.json.JSONException;
import org.json.JSONObject;

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
import cn.jpush.android.api.JPushInterface;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.LoginTask;
import cn.suanzi.baomi.base.pojo.CouponResult;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ActIcBcDetailActivity;
import cn.suanzi.baomi.cust.activity.ActThemeDetailActivity;
import cn.suanzi.baomi.cust.activity.BatchCouponDetailActivity;
import cn.suanzi.baomi.cust.activity.HomeActivity;
import cn.suanzi.baomi.cust.activity.LoginActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.fragment.ActIcBcDetailFragment;
import cn.suanzi.baomi.cust.fragment.BatchCouponDetailFragment;
import cn.suanzi.baomi.cust.util.DialogUtils;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

	private static final String TAG = MyReceiver.class.getSimpleName();
	
	private static final String KEY_FISRT_RUN = "firstrun";
	/** 登录的jpush */
	public static final int LOGIN_DATA = 1;
	/** 修改活动的标志 */
	private static final String UPDATE_ACT = "ACT_UPDATE";
	/** 登录的标志 */
	private static final String LOGIN = "LOGIN";
	/**使用优惠券码的标识*/
	private static final String PAY_COUPON_USE = "PAY_COUPON_USE";
	/** 扫描支付成功*/
	private static final String CONSUME = "CONSUME";
	/** 退款的推送标示*/
	private static final String PAY_COUPON_REFUND = "PAY_COUPON_REFUND";
	/**优惠券即将过期推送的标识*/
	private static final String COUPON_TO_BE_EXPIRED = "COUPON_TO_BE_EXPIRED";
	
	private int id = 0;
	private String title;
	private String message;
	private Bundle bundle;
	/** 活动编号 */
	private String actId;
	/** 活动名称 */
	private String activityName;
	/** 登陆的时间 */
	private String mLoginTime;
	/** 登陆的账号和密码 */
	private String userName;
	private String userPwd;
	/** 推送的状态 */
	private String mLoginJpush;
	/** 登录的推送 */
	private static int mLoginData;

	
	/**
	 * 登录的初始化
	 */
	public static void setInitLoginData() {
		mLoginData = 0; // 初始化数据
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {

			bundle = intent.getExtras();   
			Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Log.d(TAG, "Registration Id [MyReceiver] 接收 : " + regId);
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

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) { // 接收通知
				Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);
				title = bundle.getString(JPushInterface.EXTRA_TITLE);
				message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
				Log.d(TAG, "messageHHHHHHHHHHHHH=" + message);
				String actExtras = bundle.getString(JPushInterface.EXTRA_EXTRA);
					
				if (UPDATE_ACT.equals(message)) {// 修改活动
					// 自定义通知
					Notification notification = new Notification(cn.suanzi.baomi.cust.R.drawable.ic_launcher, title, System.currentTimeMillis());
					notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后自动清除图标
					notification.defaults = Notification.DEFAULT_SOUND; // 通知默认的声音
					String extras = bundle.getString(JPushInterface.EXTRA_EXTRA); // 推送的附加字段
					Log.d(TAG, "actExtras====" + actExtras);
					try {
						JSONObject extraJson = new JSONObject(actExtras);
						if (null != extraJson && extraJson.length() > 0) {
							actId = extraJson.getString(CustConst.ActDetailKey.ACT_CODE);
							Log.d(TAG, "actId========" + actId);
							activityName = extraJson.getString(CustConst.ActDetailKey.ACT_NAME);
						}

					} catch (JSONException e) {    
						e.printStackTrace();
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					int idx = ++id;
					Intent i = new Intent(context, ActIcBcDetailActivity.class);
					i.putExtra(ActIcBcDetailFragment.newInstance().ACTIVITY_CODE, actId);
					i.putExtras(bundle);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					String actClass = DB.getStr(CustConst.ActDetailKey.ACT_MAIN); // 获取活动的类
					String runActClass = DB.getStr(CustConst.ActDetailKey.ACT); // 获取运行的类
					Log.d(TAG, "actClass=" + actClass + ",runActClass=" + runActClass);
					// 判断是否在活动页运行
					String isAct = DB.getStr(CustConst.ActDetailKey.IS_ACT_MAIN);
					Log.d(TAG, "isAct☆☆=========" + isAct);
					if (null != isAct && CustConst.ActDetailKey.IS_ACT_MAIN.equals(isAct)) {
						Log.d(TAG, "不在活动页。。。。。。。。");
						showUppAct(context, idx, notification, i);
					} else {
						Log.d(TAG, "在活动页。。。。。。。。");
						if (null != actClass && !runActClass.equals(actClass)) {
							Log.d(TAG, "sdasdasdasdas");
							showUppAct(context, idx, notification, i);
						}
					}
				} else if (LOGIN.equals(message)) {// 登录
					Log.d(TAG, "loginPush >>> " + DB.getBoolean(DB.Key.CUST_LOGIN));
					if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
						// 自定义通知
						Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
						notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后自动清除图标
						notification.defaults = Notification.DEFAULT_SOUND; // 通知默认的声音
						String extras = bundle.getString(JPushInterface.EXTRA_EXTRA); // 推送的附加字段
						Log.d(TAG, "extras====" + actExtras);
						try {
							JSONObject extraJson = new JSONObject(actExtras);
							if (null != extraJson && extraJson.length() > 0) {
								mLoginTime = extraJson.getString(CustConst.LoginKey.LOGIN_TIME);
								Log.d(TAG, "mLoginTime========" + mLoginTime);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						int idx = ++id;
						Intent i = new Intent(context, LoginActivity.class);
				
						i.putExtras(bundle);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						String date = mLoginTime;// 登录的时间
						String phone = "";// 实际的型号
						showLogin();
						mLoginData = LOGIN_DATA; // 记录数
						HomeActivity.getJPushLoginData(mLoginData);
					}
				}else if(PAY_COUPON_USE.equals(message)){
					// 使用优惠券码的推送   点击通知后跳转到券使用完成界面(H5)
					// 获取NotificationManager的引用  
					/*NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
					Notification notification = new Notification(R.drawable.huiquan_logo, "兑换成功", System.currentTimeMillis());
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;  
					
					Log.d(TAG, "actExtras==="+actExtras);
					CouponResult couponResult = Util.json2Obj(actExtras, CouponResult.class);
					
					CharSequence contentTitle = "温馨提示"; 
					
					CharSequence contentText = couponResult.getContent();
					
					Intent notificationIntent = new Intent(context, ActThemeDetailActivity.class);
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
					notificationIntent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.PAY_COUPON_USE);
					
					notificationIntent.putExtra("COUPONCODE", couponResult.getCouponCode());
					
					PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);  

					// 通知状态栏显示Notification  
					final int HELLO_ID = 1;  
					mNM.notify(HELLO_ID, notification);  */
					
					Log.d(TAG, "直接跳转actExtras==="+actExtras);
					CouponResult couponResult = Util.json2Obj(actExtras, CouponResult.class);
					
					Intent useCouponCodeIntent = new Intent(context, ActThemeDetailActivity.class);
					useCouponCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
					useCouponCodeIntent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.PAY_COUPON_USE);
					useCouponCodeIntent.putExtra("COUPONCODE", couponResult.getCouponCode());
					context.startActivity(useCouponCodeIntent);
					
				} 
				// 扫描支付成功
				else if (CONSUME.equals(message)) {
					CouponResult couponResult = Util.json2Obj(actExtras, CouponResult.class);
					Intent useCouponCodeIntent = new Intent(context, ActThemeDetailActivity.class);
					useCouponCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
					useCouponCodeIntent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
					useCouponCodeIntent.putExtra(ActThemeDetailActivity.THEME_URL, couponResult.getWebUrl());
					context.startActivity(useCouponCodeIntent);
				}
				// 优惠券退款的推送
				else if (PAY_COUPON_REFUND.equals(message)) {
					
					// 获取NotificationManager的引用  
					NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
					Notification notification = new Notification(R.drawable.huiquan_logo, "退款成功", System.currentTimeMillis());
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;  
					Log.d(TAG, "actExtras==="+actExtras);
					CouponResult couponResult = Util.json2Obj(actExtras, CouponResult.class);
					CharSequence contentTitle = "温馨提示"; 
					CharSequence contentText = couponResult.getContent();
					Intent notificationIntent = new Intent(context, ActThemeDetailActivity.class);
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
					notificationIntent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
					notificationIntent.putExtra(ActThemeDetailActivity.THEME_URL, couponResult.getWebUrl());
					
					PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);  

					// 通知状态栏显示Notification  
					final int HELLO_ID = 2;  
					mNM.notify(HELLO_ID, notification); 
				}
				
				//优惠券即将过期的推送
				else if(COUPON_TO_BE_EXPIRED.equals(message)){
					NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
					Notification notification = new Notification(R.drawable.huiquan_logo, "即将过期通知", System.currentTimeMillis());
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;  
					Log.d(TAG, "actExtras==="+actExtras);
					
					CouponResult couponResult = Util.json2Obj(actExtras, CouponResult.class);
					CharSequence contentTitle = "温馨提示"; 
					CharSequence contentText = couponResult.getContent();
					Intent notificationIntent = new Intent(context, BatchCouponDetailActivity.class);
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
					notificationIntent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_CODE, couponResult.getBatchCouponCode());
					notificationIntent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_TYPE, couponResult.getCouponType());
					
					PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent); 
					
					// 通知状态栏显示Notification  
					final int HELLO_ID = 3;  
					mNM.notify(HELLO_ID, notification); 
					
				} 

			}
			
			else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				/*
				 * Log.d(TAG, "[MyReceiver] 用户点击打开了通知"); //打开自定义的Activity Intent
				 * i = new Intent(context, TestActivity.class);
				 * i.putExtras(bundle);
				 * //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
				 * Intent.FLAG_ACTIVITY_CLEAR_TOP ); context.startActivity(i);
				 */

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
				// 打开一个网页等..

			} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
			} else {
				Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}

		} catch (Exception e) {
			Log.e(TAG, "推送> error >>>" + e.getMessage());
		}
	}

	// 更改了活动内容
	private void showUppAct(Context context, int idx, Notification notification, Intent i) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, idx, i, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "惠圈", "您的" + activityName + "活动有新的更新内容，请注意查看", contentIntent);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(idx, notification);
	}

	// 重新登陆
	private void showLogin() {
		DialogUtils.show(mLoginTime, new Runnable() {
			@Override
			public void run() {
				final Activity activity = AppUtils.getActivity();// 得到当前activity
				UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
				if (userToken == null) {
					Intent intent = new Intent(activity, LoginActivity.class);
					activity.startActivity(intent);
				} else {
					userName = userToken.getMobileNbr();
					userPwd = userToken.getPassword();
					Log.i(TAG, "userName===========" + userName);
					Log.i(TAG, "userPwd===========" + userPwd);
				}

				// 传过来的registerid
				String regId = null;
				if ("".equals(regId) && regId == null) {
					regId = "";
				} else {
					regId = DB.getStr(CustConst.JPush.JPUSH_REGID);
					Log.d(TAG, "RegisterSave aa=" + regId);
				}

				new LoginTask(activity, new LoginTask.Callback() {
					@Override
					public void getResult(int result) {
						if (ErrorCode.SUCC == result) {
							HomeActivity.setJpusLoginData();
						} else {
							Log.d(TAG, "myReceiver 进来了");
							Intent it = new Intent(activity, LoginActivity.class);
							activity.startActivity(it);
							activity.finish();
						}
					}
				}).execute(userName, userPwd, regId);     
			}
		});
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		/*if (CustApplication.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(CustApplication.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(CustApplication.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(CustApplication.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}*/

	}
}
