package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.cashier.TransInstallmentFragment;

import android.app.Fragment;

/**
 * @author hsm
 * 收银台-分期付款交易
 */
public class CashierInstallmentTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransInstallmentFragment.newInstance();
	}
}
