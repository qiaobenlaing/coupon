package cn.suanzi.baomi.shop.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.fragment.UpdateCampaignFreeFragment;

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
