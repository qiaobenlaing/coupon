// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.view.StatusBarView;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;

import cn.jpush.android.api.JPushInterface;

/**
 * 用于显示广告和检查更新
 *
 * @author
 */
public class StartActivity extends Activity {

	/**
	 * 登录的用户名和密码
	 */
	private String mUserName;
	private String mUserPwd;

	private final int TIME = 2000;//闪屏时间：毫秒

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		StatusBarView.statusBar(this);
		//添加
		Util.addActivity(StartActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}

	private void init() {

		/** 判断是否为第一次使用 */
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstRun = sharedPreferences.getBoolean(DB.Key.SHOP_IS_FIRST_RUN, true);
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {//若为第一次使用
			editor.putBoolean(DB.Key.SHOP_IS_FIRST_RUN, false);//修改信息
			editor.apply();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(StartActivity.this, GuideActivity.class);
					startActivity(intent);
					finish();
				}
			}, TIME);
		} else {//非第一次使用
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					//得到登录的用户名和密码
					SharedPreferences mSharedPreferences = getSharedPreferences(ShopConst.LoginSave.LOGIN_KEEP, Context.MODE_PRIVATE);
					String mobileNbr = mSharedPreferences.getString("mobileNbr", "");
					String password = mSharedPreferences.getString("password", "");

					UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
					//传过来的registerid
					String regId = DB.getStr(ShopConst.RegisterSave.JPUSH_REGID);

					if (Util.isEmpty(mobileNbr) || Util.isEmpty(password)) {
						startActivity(new Intent(StartActivity.this, LoginActivity.class));
						finish();
					} else {
						if (userToken == null) {
							startActivity(new Intent(StartActivity.this, LoginActivity.class));
							finish();
						} else {
							mUserName = userToken.getMobileNbr();
							mUserPwd = userToken.getPassword();
							if (mobileNbr.equals(mUserName) && password.equals(mUserPwd)) {
								//是否联网状态
								if (Util.isNetworkOpen(StartActivity.this)) {
									// 执行登录
									new LoginTask(StartActivity.this, new LoginTask.Callback() {
										@Override
										public void getResult(int result) {
											//当返回错误信息时
											if (result == ErrorCode.FAIL) {
												startActivity(new Intent(StartActivity.this, LoginActivity.class));
												finish();
											}
										}
									}, HomeActivity.class, Const.Login.ANDR_B_HOMEFRAGMENT).execute(mobileNbr, password, regId);
								} else {
									Toast.makeText(StartActivity.this, R.string.toast_login_internet, Toast.LENGTH_SHORT).show();
									startActivity(new Intent(StartActivity.this, LoginActivity.class));
									finish();
								}
							} else {
								Intent intent = new Intent(StartActivity.this, LoginActivity.class);
								startActivity(intent);
								finish();
							}
						}
					}
				}
			}, TIME);
		}
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
}

