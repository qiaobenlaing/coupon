package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.CampaignIntroducesFragment;

import android.app.Fragment;
import android.view.KeyEvent;

/**
 * 活动介绍
 * @author wensi.yu
 *
 */
public class CampaignIntroducesActivity extends SingleFragmentActivity {
	
	private CampaignIntroducesFragment mCampaignIntroducesFragment;

	@Override
	protected Fragment createFragment() {
		mCampaignIntroducesFragment =  CampaignIntroducesFragment.newInstance();
		return mCampaignIntroducesFragment;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
