package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.cashier.TransQueryFragment;

import android.app.Fragment;

/**
 * @author hsm
 * 收银台-查询明细交易
 */
public class CashierQueryTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransQueryFragment.newInstance();
	}
}
