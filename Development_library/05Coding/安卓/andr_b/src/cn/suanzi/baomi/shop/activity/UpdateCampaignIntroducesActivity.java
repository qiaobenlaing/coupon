package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.UpdateCampaignIntroducesFragment;

/**
 * 活动介绍
 * @author wensi.yu
 *
 */
public class UpdateCampaignIntroducesActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return UpdateCampaignIntroducesFragment.newInstance();
	}

}
