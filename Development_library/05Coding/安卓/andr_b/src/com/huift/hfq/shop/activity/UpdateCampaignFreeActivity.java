package com.huift.hfq.shop.activity;

import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.fragment.UpdateCampaignFreeFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.huift.hfq.shop.R;

/**
 * 修改活动收费
 * @author wensi.yu
 */
public class UpdateCampaignFreeActivity extends Activity{
	
	private UpdateCampaignFreeFragment mUpdateCampaignFreeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_camapign_free);
		FragmentManager mFragmentManager = getFragmentManager();
		mUpdateCampaignFreeFragment = UpdateCampaignFreeFragment.newInstance();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.add(R.id.ly_campaign, mUpdateCampaignFreeFragment);
		trx.commit();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
