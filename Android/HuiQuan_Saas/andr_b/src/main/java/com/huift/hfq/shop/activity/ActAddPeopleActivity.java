package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActAddPeopleFragment;

import android.app.Fragment;

/**
 * @author wensi.yu
 * 营销活动中的人数限制
 */
public class ActAddPeopleActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ActAddPeopleFragment.newInstance();
	}
}
