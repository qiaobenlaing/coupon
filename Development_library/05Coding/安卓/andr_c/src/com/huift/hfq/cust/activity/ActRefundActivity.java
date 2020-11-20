package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ActRefundFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 活动退款
 * @author yanfang.li
 */
public class ActRefundActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ActRefundFragment.newInstance();
	}
}
