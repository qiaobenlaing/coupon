package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.NewShopRecentVisitFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

public class NewShopRecentVisistActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return NewShopRecentVisitFragment.newInstance();
	}

}
