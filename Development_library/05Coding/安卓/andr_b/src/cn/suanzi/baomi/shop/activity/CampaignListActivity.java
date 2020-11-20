package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import android.content.Intent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.CampaignListFragment;

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
