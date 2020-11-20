package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyHomeAboutFragment;

import android.app.Fragment;

public class MyHomeAboutActivity  extends SingleFragmentActivity{
	private final static String TAG = "MyHomeAboutActivity";
	@Override
	protected Fragment createFragment() {
		return MyHomeAboutFragment.newInstance();
	}
}
