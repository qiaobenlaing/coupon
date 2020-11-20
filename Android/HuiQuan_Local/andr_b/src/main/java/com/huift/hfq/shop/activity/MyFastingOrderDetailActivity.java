package com.huift.hfq.shop.activity;

import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.fragment.MyFlatingOrderDetailFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import com.huift.hfq.shop.R;
 
/**
 * 餐前订单详情页面（待接单）
 * @author qian.zhou
 */
public class MyFastingOrderDetailActivity extends Activity {
	private static final String TAG = "MyFastingOrderDetailActivity";
	private MyFlatingOrderDetailFragment mFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flatlist);
		mFragment = MyFlatingOrderDetailFragment.newInstance();
		/** Fragment管理器 */
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.replace(R.id.ly_flatlist, mFragment);
		trx.addToBackStack(null);
		trx.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	@Override
	protected void onResume() {
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
			MyFastingOrderDetailActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
