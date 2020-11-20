package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.CampaignAddFragment;

/**
 * 新建活动
 * @author wensi.yu
 *
 */
public class CampaignAddActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return CampaignAddFragment.newInstance();
	}

}
