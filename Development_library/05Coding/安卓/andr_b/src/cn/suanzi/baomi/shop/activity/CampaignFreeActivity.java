package cn.suanzi.baomi.shop.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.fragment.CampaignFreeFragment;
import cn.suanzi.baomi.shop.fragment.MyStaffManagementFragment;

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
