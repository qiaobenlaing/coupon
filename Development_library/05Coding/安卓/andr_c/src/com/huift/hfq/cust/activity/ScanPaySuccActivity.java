package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ScanPaySuccFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 扫描支付成功界面（推送界面）
 * @author yanfang.li
 */
public class ScanPaySuccActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ScanPaySuccFragment.newInstance();
	}
}
