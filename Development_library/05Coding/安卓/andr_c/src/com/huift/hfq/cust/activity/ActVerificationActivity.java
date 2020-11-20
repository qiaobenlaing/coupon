package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.EnrollFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 活动验证
 * @author qian.zhou
 */
public class ActVerificationActivity extends SingleFragmentActivity {

	private final static String TAG = ActVerificationActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return EnrollFragment.newInstance();
	}
}
