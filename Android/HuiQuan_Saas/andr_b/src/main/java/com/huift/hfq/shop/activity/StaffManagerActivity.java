package com.huift.hfq.shop.activity;


import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.fragment.StaffManagerFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.huift.hfq.shop.R;

/**
 * 店员管理
 * @author qian.zhou
 */
public class StaffManagerActivity extends Activity {
	
	private StaffManagerFragment mFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managerlist);
		mFragment = StaffManagerFragment.newInstance();
		/** Fragment管理器 */
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.replace(R.id.ly_stafflist, mFragment);
		trx.addToBackStack(null);
		trx.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != mFragment) {
			mFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
	
	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			StaffManagerActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
