package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActAddMoneyFragment;

import android.app.Fragment;

/**
 * 添加红包
 * 
 * @author wensi.yu
 *
 */
public class ActAddMoneyActivity extends SingleFragmentActivity {

	private final static String TAG = "ActAddMoneyActivity";
	
	@Override
	protected Fragment createFragment() {
		return ActAddMoneyFragment.newInstance();
	}
}
