package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.CampaignIntroducesFragment;

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
