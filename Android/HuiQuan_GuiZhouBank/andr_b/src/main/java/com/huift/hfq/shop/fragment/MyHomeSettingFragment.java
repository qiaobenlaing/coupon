package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.LoginActivity;
import com.huift.hfq.shop.model.GetShopStaffSettingTask;
import com.huift.hfq.shop.model.LogoffTask;
import com.huift.hfq.shop.model.UpdateShopStaffSettingTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

/**
 * 设置
 * @author qian.zhou
 */
public class MyHomeSettingFragment extends Fragment {
	public static final String BROADCAST_ON = "1";
	public static final String BROADCAST_OFF = "0";
	public static final String MSGBING_ON = "1";
	public static final String MSGBING_OFF = "0";
	public static final String COUPONMSG_ON = "1";
	public static final String COUPONMSG_OFF = "0";
	private static final String APP_TYPE = "0";
	/** 注册id*/
	private String mRegistrationId;
	/** 退出 **/
	private LinearLayout mLyExit;
	/** 是否接收支付短信*/
	private ImageView mCkPayMsg;
	/** 标示用户传递的是否支持接收支付短信(1.接收 0.不接收)*/
	private String mIsSetting;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	/** 是否接受支付短信*/
	private String mIsCkPayMsg;
	
	public static MyHomeSettingFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeSettingFragment fragment = new MyHomeSettingFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myhome_setting, container, false);
		ViewUtils.inject(this, v);
		Util.addHomeActivity(getActivity());
		Util.addLoginActivity(getActivity());
		init(v);
		return v;
	}

	private void init(View v) {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		//注册id
		mRegistrationId = DB.getStr(ShopConst.JPUSH_REGID);
		//标题设置
		LinearLayout ivTurnin = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		ivTurnin.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(R.string.app_set);
		//退出
		mLyExit = (LinearLayout) v.findViewById(R.id.ly_myhome_exit);
		mLyExit.setOnClickListener(exitListener);
		//是否接收支付短信
		mCkPayMsg = (ImageView) v.findViewById(R.id.iv_ispaymsg);
		mCkPayMsg.setOnClickListener(checkListener); 
		//查询登录用户是否接收支付短信
		getShopStaffSetting();
	}
	
    // 点击退出
	OnClickListener exitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.quit_ok), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
				@Override
				public void onOK() {
					logoff();
				}
			});
		}
	};
	
	OnClickListener checkListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mSettledflag) {
				if (String.valueOf(Util.NUM_ONE).equals(mIsCkPayMsg)) { 
					 mIsSetting = "0";
					 mCkPayMsg.setImageResource(R.drawable.checkbox_empty);
					 mIsCkPayMsg = mIsSetting;
					
			     } else { 
			    	 mIsSetting = "1"; 
			    	 mCkPayMsg.setImageResource(R.drawable.checkbox_check);
			    	 mIsCkPayMsg = mIsSetting;
			     } 
				 updateShopStaffSetting();
			} else {
				mShopApplication.getDateInfo(getActivity());
			}
		}
	};
	
	/**
	 * 查询该用户是否接收支付短信
	 */
	public void getShopStaffSetting(){
		new GetShopStaffSettingTask(getActivity(), new GetShopStaffSettingTask.Callback() {
			@Override
			public void getResult(JSONObject JSONobject) {
				mIsCkPayMsg = JSONobject.get("isSendPayedMsg").toString();
				if (!Util.isEmpty(mIsCkPayMsg)) {
					mCkPayMsg.setImageResource(String.valueOf(Util.NUM_ONE).equals(mIsCkPayMsg) ? R.drawable.checkbox_check : R.drawable.checkbox_empty);
				}
			}
		}).execute();
	}
	
	/**
	 * 修改登录用户是否接收支付短信
	 */
	public void updateShopStaffSetting(){
		new UpdateShopStaffSettingTask(getActivity(), new UpdateShopStaffSettingTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
					Util.getContentValidate(R.string.toast_upp_succ);
				} else {
					Util.getContentValidate(R.string.set_error);
				}
			}
		}).execute(mIsSetting);
	}
		
	/**
	 * 退出登录
	 */
	public void logoff(){
		new LogoffTask(getActivity(), new LogoffTask.Callback() {
			@Override
			public void getResult(JSONObject object) {
				if (object == null) {
					return ;
				}  else {
					if (String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())) {
						SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(ShopConst.LOGIN_KEEP, Context.MODE_PRIVATE);
		                Editor editor = mSharedPreferences.edit(); 
		                String mobileNbr = mSharedPreferences.getString("mobileNbr", "");
		    			String password = mSharedPreferences.getString("password", "");
						String zoneId = mSharedPreferences.getString("zoneId", "");
		    			editor.clear();
		    			editor.commit();
		            	startActivity(new Intent(getActivity(), LoginActivity.class));
		            	Util.exitHome();
					} else {
						Util.showToastZH("退出不成功");
					}
				}
			}
		}).execute(APP_TYPE,mRegistrationId);
	}
		
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.layout_turn_in })
	private void ivreturnClickTo(View v) {
		getActivity().finish();
	}
}
