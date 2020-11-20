package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ResetPwdFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 用于重置密码
 * @author Wei.Fang
 * 
 */
public class ResetPwdActivity extends SingleFragmentActivity {
	
	private static final String TAG = ResetPwdActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "ResetPwdActivity Create");
	}

	@Override
	protected Fragment createFragment() {
		return ResetPwdFragment.newInstance();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "ResetPwdActivity Destroy");
	}
}
