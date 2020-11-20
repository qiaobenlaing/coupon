package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ICBCCashPayFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

public class ICBCCashPayActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ICBCCashPayFragment.newInstance();
	}

}
