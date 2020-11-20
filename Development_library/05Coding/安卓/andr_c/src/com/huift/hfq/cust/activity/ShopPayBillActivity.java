package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ShopPayBillFragment;

import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import com.huift.hfq.base.SingleFragmentActivity;

public class ShopPayBillActivity extends SingleFragmentActivity{

	private static final String TAG = ShopPayBillActivity.class.getSimpleName();
	private ShopPayBillFragment mFragment; 
	@Override
	protected Fragment createFragment() {
		mFragment = ShopPayBillFragment.newInstance();
		return mFragment;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("TAG", "aaaaaaaaaaaaaaaaaaaa");
		if (mFragment != null) {
			mFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
}
