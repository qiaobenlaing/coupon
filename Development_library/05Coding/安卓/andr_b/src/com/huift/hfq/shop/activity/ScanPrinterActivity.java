package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ScanPrinterFragment;

import android.app.Fragment;

/**
 * 打印
 * @author wensi.yu
 *
 */
public class ScanPrinterActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ScanPrinterFragment.newInstance();
	}

}
