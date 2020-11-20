package com.huift.hfq.shop.activity;

import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.fragment.CampaignFreeFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.huift.hfq.shop.R;

/**
 * 活动收费
 * @author wensi.yu
 *
 */
public class CampaignFreeActivity extends Activity{
	
	private CampaignFreeFragment mCampaignFreeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camapign_free);
		FragmentManager mFragmentManager = getFragmentManager();
		mCampaignFreeFragment = CampaignFreeFragment.newInstance();
		FragmentTransaction trx= mFragmentManager.beginTransaction();
		trx.add(R.id.ly_campaign, mCampaignFreeFragment);
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
