package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyHomeSettingFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

public class MyHomeSettingActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return MyHomeSettingFragment.newInstance();
	}
}
