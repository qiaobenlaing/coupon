package com.huift.hfq.shop.activity;

import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.fragment.BillClassFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import com.huift.hfq.shop.R;

/**
 * 账单的列表
 * @author wensi.yu
 *
 */
public class BillClassActivity extends Activity {

	private BillClassFragment mBillClassFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billclass);
		FragmentManager mFragmentManager = getFragmentManager();
		mBillClassFragment = BillClassFragment.newInstance();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.add(R.id.ly_billclass, mBillClassFragment);
		trx.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public BillClassFragment getmBillClassFragment() {
		return mBillClassFragment;
	}
}
