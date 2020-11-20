package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.CampaignAddFragment;

import android.app.Fragment;

/**
 * 新建活动
 * @author wensi.yu
 *
 */
public class CampaignAddActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return CampaignAddFragment.newInstance();
	}

}
