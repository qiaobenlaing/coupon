package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ActDetailFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 活动管理
 * @author yanfang.li
 *
 */
public class ActDetailActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ActDetailFragment.newInstance();
	}
}
