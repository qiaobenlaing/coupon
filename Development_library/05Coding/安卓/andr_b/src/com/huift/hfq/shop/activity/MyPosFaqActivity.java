package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyPosFaqFragment;

import android.app.Fragment;

/**
 * POS FAQ
 * @author qian.zhou
 */
public class MyPosFaqActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyPosFaqFragment.newInstance();
	}
}
