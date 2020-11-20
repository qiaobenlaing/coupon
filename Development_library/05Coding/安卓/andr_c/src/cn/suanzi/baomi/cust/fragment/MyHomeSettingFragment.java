package cn.suanzi.baomi.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.LoginTask;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.LoginActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.LogoffTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

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
	private static final String APP_TYPE = "1";
	/** 注册id*/
	private String regId = "";
	/** 退出 **/
	private RelativeLayout mLyExit;

	private static final String TAG = MyHomeSettingFragment.class.getSimpleName();
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
		Util.addHomeActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(v);
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View v) {
		// 退出
		mLyExit = (RelativeLayout) v.findViewById(R.id.ly_myhome_exit);
		mLyExit.setOnClickListener(exitListener);
		
		//标题
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(R.string.myhome_set);
		
		//传过来的registerid
		regId = null;
		if (Util.isEmpty(regId)) {
			regId = "";
		} else {
			regId = DB.getStr(CustConst.JPush.JPUSH_REGID);
			Log.d(TAG,"RegisterSave aa="+regId);
		}
	}

	// 点击退出
	OnClickListener exitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			DialogUtils.showDialog(getActivity(),Util.getString(R.string.cue),Util.getString(R.string.quit_ok),Util.getString(R.string.ok),Util.getString(R.string.no), 
					new DialogUtils().new OnResultListener() {
				@Override
				public void onOK() {
					logoff();
				}
			});
		}
	};

	/**
	 * 退出登录
	 */
	public void logoff() {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String tokenCode = userToken.getTokenCode();
		Log.d(TAG, "tokenCode=="+tokenCode);
		
		//传过来的registerid
		String regId = null;
		if ("".equals(regId) && regId == null) {
			regId = "";
		} else {
			regId = DB.getStr(CustConst.JPush.JPUSH_REGID);
			Log.d(TAG,"RegisterSave aa>>>>>>>>>>>>>>>>>>>="+regId);
		}
		new LogoffTask(getMyActivity(), new LogoffTask.Callback() {
			@Override
			public void getResult(JSONObject object) {
				if (object == null) {
					return;
				} else {
					if (String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())) {
						SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences(CustConst.LoginSave.LOGIN_KEEP, Context.MODE_PRIVATE);
						Editor editor = mSharedPreferences.edit();
						editor.clear();
						editor.commit();
						DB.saveBoolean(DB.Key.CUST_LOGIN, false);
		    			Intent it = new Intent(getMyActivity(), LoginActivity.class);
		    			it.putExtra(LoginTask.ALL_LOGIN, Const.Login.EXIT_LOGIN);
		    			getMyActivity().startActivity(it);
		    			getMyActivity().finish();
		    			Util.exitHome();
					} else {
						Util.getContentValidate(R.string.app_exit_fail);
					}
				}
			}
		}).execute(tokenCode, APP_TYPE,regId);
	}

	@OnClick({ R.id.iv_turn_in })
	private void click(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			getActivity().finish();
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeSettingFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeSettingFragment.class.getSimpleName());
	}
}
