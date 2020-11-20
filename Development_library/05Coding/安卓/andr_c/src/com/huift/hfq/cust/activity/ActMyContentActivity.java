package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ActMyContentFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 我的活动列表
 * @author wensi.yu
 *
 */
public class ActMyContentActivity extends SingleFragmentActivity {
	
	private static final String TAG = "ActMyContentActivity";

	@Override
	protected Fragment createFragment() {
		return ActMyContentFragment.newInstance();
	}

}
