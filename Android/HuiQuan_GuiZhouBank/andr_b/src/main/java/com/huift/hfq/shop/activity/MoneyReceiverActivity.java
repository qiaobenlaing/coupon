package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MoneyReceiverFragment;

import android.app.Fragment;

/**
 * 红包领取人数
 * @author wensi.yu
 *
 */
public class MoneyReceiverActivity extends SingleFragmentActivity {

	private final static String TAG = "MoneyReceiverActivity";
	
	@Override
	protected Fragment createFragment() {
		return MoneyReceiverFragment.newInstance();
	}

}
