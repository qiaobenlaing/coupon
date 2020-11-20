package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.CampaignListFragment;

import android.app.Fragment;
import android.content.Intent;

/**
 * 活动列表
 * @author wensi.yu
 *
 */
public class CampaignListActivity extends SingleFragmentActivity{

	private CampaignListFragment mCampaignListFragment;
	
	@Override
	protected Fragment createFragment() {
		return mCampaignListFragment.newInstance();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != mCampaignListFragment) {
			mCampaignListFragment.onActivityResult(requestCode, resultCode, data);
		}
	}

}
