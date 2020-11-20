package com.huift.hfq.cust.application;

import com.huift.hfq.cust.util.ExampleUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.SzApplication;

/**
 * Cust Application
 * 
 * @author Zhonghui.Dong
 */
public class CustApplication extends SzApplication {
	/** Debug TAG */
	private static final String TAG = CustApplication.class.getSimpleName();
	/** isLogin 判断用户是否在登录状态，是登录后轮询才访问数 */
	public boolean isLogin = false;
	/** TODO*/
	public static boolean isForeground = false;

	@Override
	public void onCreate() {
		super.onCreate();
		this.currAppType = Const.AppType.CUST;
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        isForeground = true;
	}
	
	//用于接收来自服务器jpush客户信息
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		this.registerReceiver(mMessageReceiver, filter);
	}
	
	/**
	 * jPush的广播接受者
	 * @author ad
	 *
	 */
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              Log.d(TAG, "extrasid"+extras);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(messge + "");
              if (!ExampleUtil.isEmpty(extras)) {
            	  showMsg.append(extras + "");
              }
              setCostomMsg(messge,extras);
			}
		}
	}
	
	private void setCostomMsg(String msg,String extras){
		 Log.d(TAG, "extras");
	}
	@Override
	public void onTerminate() {
		Log.d(TAG, "Application Terminate");
		super.onTerminate();
	}
	

}
