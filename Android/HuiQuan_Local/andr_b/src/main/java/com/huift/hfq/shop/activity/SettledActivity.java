package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.SettledFragment;

import android.app.Fragment;

/**
 * 我要入驻
 * @author wensi.yu
 *
 */
public class SettledActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return SettledFragment.newInstance();
	}

}
