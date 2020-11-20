package com.huift.hfq.shop.activity;

import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.fragment.UppDeliveryProgramFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import com.huift.hfq.shop.R;

/**
 * 修改商家配送方案
 * @author qian.zhou 
 */
public class UppDeliveryProgramActivity extends Activity{
	private UppDeliveryProgramFragment mDeliveryProgramFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upp_shopdeliverylist);
		/** Fragment管理器 */
		FragmentManager mFragmentManager = getFragmentManager();
		mDeliveryProgramFragment = UppDeliveryProgramFragment.newInstance();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.add(R.id.ly_deliverylist, mDeliveryProgramFragment);
		trx.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
	
	/**
	 * 点击返回
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UppDeliveryProgramActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
