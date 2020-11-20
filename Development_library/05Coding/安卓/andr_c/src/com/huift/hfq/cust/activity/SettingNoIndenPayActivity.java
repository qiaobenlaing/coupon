package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.SettingNoIndenPayFragment;

import android.app.Fragment;
import android.view.KeyEvent;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 设置免验证码支付
 * @author yingchen
 *
 */
public class SettingNoIndenPayActivity extends SingleFragmentActivity {

	private SettingNoIndenPayFragment fragment;
	@Override
	protected Fragment createFragment() {
		 fragment = SettingNoIndenPayFragment.newInstance();
		 return fragment;
	}

	
	/**
		 * 点击返回
		 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		fragment.onBack();
		return true;
	}
}
