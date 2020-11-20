package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ScanBarCodeFragment;

import android.app.Fragment;
import android.view.KeyEvent;

/**
 * 机器扫描
 * @author wensi.yu
 *
 */
public class ScanBarCodeActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return ScanBarCodeFragment.newInstance();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

}
