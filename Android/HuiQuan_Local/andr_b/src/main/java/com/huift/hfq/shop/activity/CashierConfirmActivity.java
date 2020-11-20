package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.cashier.CashierConfirmFragment;

import android.app.Fragment;

/**
 * @author hsm
 * 收银台-操作确认
 */
public class CashierConfirmActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return CashierConfirmFragment.newInstance();
	}
}
