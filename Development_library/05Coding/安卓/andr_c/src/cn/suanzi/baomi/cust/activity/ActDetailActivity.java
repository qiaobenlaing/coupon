package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ActDetailFragment;

/**
 * 活动管理
 * @author yanfang.li
 *
 */
public class ActDetailActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ActDetailFragment.newInstance();
	}
}
