package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.CampaignTypeFragment;

import android.app.Fragment;

/**
 * 活动类型
 * @author wensi.yu
 *
 */
public class CampaignTypeActivity extends SingleFragmentActivity {
	

	@Override
	protected Fragment createFragment() {
		return CampaignTypeFragment.newInstance();
	}
	

}
