package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ScanInputAmountFragment;

import android.app.Fragment;

/**
 * 扫描输入金额
 * @author wensi.yu
 *
 */
public class ScanInputAmountActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return ScanInputAmountFragment.newInstance();
	}

}
