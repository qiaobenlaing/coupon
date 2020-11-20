package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyHomeBaseInfoFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

public class MyHomeBaseInfoActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return MyHomeBaseInfoFragment.newInstance();
	}

}
