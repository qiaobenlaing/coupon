package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyBalanceManagerSusFragment;

import android.app.Fragment;

public class MyBalanceManagerSusActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return MyBalanceManagerSusFragment.newInstance();
	}
}
