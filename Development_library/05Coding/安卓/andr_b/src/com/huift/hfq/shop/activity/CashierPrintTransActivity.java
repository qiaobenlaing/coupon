package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.cashier.TransPrintFragment;

import android.app.Fragment;

/**
 * @author hsm
 * 收银台-打印交易
 */
public class CashierPrintTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransPrintFragment.newInstance();
	}
}
