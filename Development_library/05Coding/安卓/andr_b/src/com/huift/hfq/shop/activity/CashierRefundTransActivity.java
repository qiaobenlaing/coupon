package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.cashier.TransRefundFragment;

import android.app.Fragment;

/**
 * @author hsm
 * 收银台-退货交易
 */
public class CashierRefundTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransRefundFragment.newInstance();
	}
}
