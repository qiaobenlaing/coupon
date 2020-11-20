package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.BillHomeFragment;

import android.app.Fragment;

public class BillHomeActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return BillHomeFragment.newInstance();
	}

}
