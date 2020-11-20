package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ActAddPeopleFragment;

/**
 * @author wensi.yu
 * 营销活动中的人数限制
 */
public class ActAddPeopleActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ActAddPeopleFragment.newInstance();
	}
}
