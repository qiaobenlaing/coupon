package com.huift.hfq.shop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.EConsuming;
import com.huift.hfq.shop.fragment.HomeFragment;
import com.huift.hfq.shop.utils.ExampleUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.huift.hfq.shop.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import cn.jpush.android.api.JPushInterface;

/**
 * Shop Application
 * . 
 * 
 * @author Weiping
 */
public class ShopApplication extends SzApplication {

	//smartRefreshLayout header与footer设置
	static {
		//设置全局的Header构建器
		SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
			@Override
			public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
				layout.setPrimaryColorsId(R.color.hint_light_gray, R.color.gray);//全局设置主题颜色
				return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
			}
		});
		//设置全局的Footer构建器
		SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
			@Override
			public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
				//指定为经典Footer，默认是 BallPulseFooter
				return new ClassicsFooter(context).setDrawableSize(20);
			}
		});
	}
	
	/** isLogin 判断用户是否在登录状态，是登录后轮询才访问数*/
	public boolean isLogin = false;
	/** TODO*/
	public static boolean isForeground = false;
	private static final String JOIN_ACT = "JOIN_ACTIVITY";
	private static final String EXIT_ACT = "EXIT_ACTIVITY";
	private static final String CONSUME = "CONSUME";
	private static final String LOGIN  = "LOGIN";
	/**  Debug TAG  */
	private static final String TAG = ShopApplication.class.getSimpleName();
	/** 订单集合*/
	private List<EConsuming> mEListData;
	/** 首页*/
	private HomeFragment mHomeFragment;
	/** 商家是否入驻*/
	private boolean settledflag = true;
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.currAppType = Const.AppType.SHOP;
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
	    JPushInterface.init(this);     		// 初始化 JPush
	    JPushInterface.setLatestNotificationNumber(this, 5);
	    //JPushInterface.setAlias(arg0, arg1, arg2)
	    // 将Push推送
		registerMessageReceiver();
		mEListData = new ArrayList<EConsuming>();
		isForeground = true;
		//settledflag = true;
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
		
		/**支付的广播*/
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.abel.action.broadcast");
        MyBroadcastReciver myReciver = new MyBroadcastReciver();
        this.registerReceiver(myReciver, intentFilter);
	}
	
	/**
	 * Mpos支付接收广播
	 */
	private class MyBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.e("PosPayFragment", "action:>>>>>>>" + action);
			if (action.equals("cn.abel.action.broadcast")) {
				String RecvData = intent.getStringExtra("RecvData");
				// 在控制台显示接收到的广播内容
				Log.e("PosPayFragment", "RecvData:>>>>>>>" + RecvData);
				if (!Util.isEmpty(RecvData)) {
					setMposPay(RecvData);
				}
				// 在android端显示接收到的广播内容
//				Toast.makeText(context, RecvData, 1).show();
			}
		}
	}
	
	private void setMposPay (String recvData) {
		if(mHomeFragment!= null){
			mHomeFragment.mPosPay (recvData);
		}
	}
	
	private void setMessage (String recvData) {
		if(mHomeFragment!= null){
			mHomeFragment.setMessage (recvData);
		}
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
              if (JOIN_ACT.equals(messge)) {//报名活动
            	  
              } else if (EXIT_ACT.equals(messge)) {//退出活动
            	  
              }  else if (CONSUME.equals(messge)) {//订单
            	  setCostomMsg(messge,extras);
            	  
              }  else if (LOGIN.equals(messge)) {//登录
            	  
              }  else if (ShopConst.ShopMessage.INVITE_SHOP.equals(messge)) {//邀请入驻惠圈
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
	  					String msg = extraJson.getString(ShopConst.Massage.CONTENT);
	  					Log.d(TAG, "msg========"+msg);
	  					setMessage(msg);
	  				}
				} catch (JSONException e) {
					e.printStackTrace();
				} 
          	  } else if (ShopConst.ShopMessage.REMIND_SHOP.equals(messge)) {//添加商品展示
          		try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
	  					String msg = extraJson.getString(ShopConst.Massage.CONTENT);
	  					Log.d(TAG, "msg========"+msg);
	  					setMessage(msg);
	  				}
				} catch (JSONException e) {
					e.printStackTrace();
				}
          	 }  
			}
		}
	}
	
	private void setCostomMsg(String msg,String extras){
		 try {
			JSONObject jPushObj = new JSONObject(extras);
			EConsuming consuming = Util.json2Obj(jPushObj.toString(), EConsuming.class);
			Log.d(TAG, consuming.getBonusPay()+"=consuming");
			Log.d(TAG, consuming.getIdentityCode()+"=1consuming");
			mEListData.add(0, consuming);
			if(mHomeFragment!= null){
				mHomeFragment.viewJpush();
				DB.saveBoolean(ShopConst.Key.JPUSH_CARDACCEPT, true);
				DB.saveBoolean(ShopConst.Key.JPUSH_COUPONACCEPT, true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}	
	
	 //获取共享的数据  
    public List<EConsuming> getShareData() { 
        return mEListData; 
    } 
    
	@Override 
	public void onTerminate(){
		Log.d(TAG, "Application Terminate");
		super.onTerminate();
	}

	public void setHomeFragment(HomeFragment homeFragment) {
		
		this.mHomeFragment = homeFragment;
	}

	public boolean getSettledflag() {
		return settledflag;
	}

	public void setSettledflag(boolean settledflag) {
		this.settledflag = settledflag;
	}
	
	/**
	 * 提示的消息
	 */
	public void getDateInfo(Activity activity) {
		Util.getContentValidate(R.string.shop_no_settled);
	}
	
}
