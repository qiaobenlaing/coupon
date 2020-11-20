package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.cashier.CashierIndexFragment;

import android.app.Fragment;

/**
 * @author hsm
 * 收银台首页
 */
public class CashierActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return CashierIndexFragment.newInstance();
	}
}
