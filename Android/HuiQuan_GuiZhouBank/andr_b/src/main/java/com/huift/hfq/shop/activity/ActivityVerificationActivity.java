package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActivityVerificationFragment;

import android.app.Fragment;

/**
 * 活动验证
 * @author qian.zhou
 */
public class ActivityVerificationActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ActivityVerificationFragment.newInstance();
	}
}
