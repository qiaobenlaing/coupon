package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.cashier.TransCancelFragment;

import android.app.Fragment;

/**
 * @author hsm
 * 收银台-撤销交易
 */
public class CashierCancelTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransCancelFragment.newInstance();
	}
}
