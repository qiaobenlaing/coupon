package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.LoginFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.base.model.LoginTask;

/**
 * 登录Activity 单Fragment
 * 
 * @author Wei.Fang
 */
public class LoginActivity extends SingleFragmentActivity {
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	/** 登录的类型*/
	private String mLoginType; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "LoginActivity create");
		try {
			mLoginType = this.getIntent().getStringExtra(LoginTask.ALL_LOGIN);
			Log.d(TAG, "loginStatus11:" + mLoginType);
		} catch (Exception e) {
			mLoginType = "";
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "LoginActivity destory");
	}
	
	public void onBackPressed(){
		if (null != mLoginType && mLoginType.equals(Const.Login.EXIT_LOGIN)) {
			Intent intent = new Intent(this,HomeActivity.class);
			startActivity(intent);
			finish();
		} else {
			finish();
		}
	
	}

	@Override
	protected Fragment createFragment() {
		return LoginFragment.newInstance();
	}
}
