package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.UpdateCampaignIntroducesFragment;

import android.app.Fragment;

/**
 * 活动介绍
 * @author wensi.yu
 *
 */
public class UpdateCampaignIntroducesActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return UpdateCampaignIntroducesFragment.newInstance();
	}

}
