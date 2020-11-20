package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.UpdateCampaignTypeFragment;

import android.app.Fragment;
import android.view.KeyEvent;

/**
 * 修改活动类型
 * @author qian.zhou
 */
public class UpdateCampaignTypeActivity extends SingleFragmentActivity {
	
	private UpdateCampaignTypeFragment mUpdateCampaignTypeFragment;

	@Override
	protected Fragment createFragment() {
		mUpdateCampaignTypeFragment =  UpdateCampaignTypeFragment.newInstance();
		return mUpdateCampaignTypeFragment;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mUpdateCampaignTypeFragment.getCampaignValue();
		}
		return super.onKeyDown(keyCode, event);
	}

}
