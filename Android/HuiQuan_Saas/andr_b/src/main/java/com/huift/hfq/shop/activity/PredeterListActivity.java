package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.PredeterListFragment;

import android.app.Fragment;

/**
 * 預定名單
 * @author qian.zhou
 */
public class PredeterListActivity extends SingleFragmentActivity {

	private final static String TAG = PredeterListActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return PredeterListFragment.newInstance();
	}
}
