package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.CampaignTypeFragment;

/**
 * 活动类型
 * @author wensi.yu
 *
 */
public class CampaignTypeActivity extends SingleFragmentActivity {
	

	@Override
	protected Fragment createFragment() {
		return CampaignTypeFragment.newInstance();
	}
	

}
