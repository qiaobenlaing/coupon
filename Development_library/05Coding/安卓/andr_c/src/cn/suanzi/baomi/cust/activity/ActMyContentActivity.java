package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ActMyContentFragment;

/**
 * 我的活动列表
 * @author wensi.yu
 *
 */
public class ActMyContentActivity extends SingleFragmentActivity {
	
	private static final String TAG = "ActMyContentActivity";

	@Override
	protected Fragment createFragment() {
		return ActMyContentFragment.newInstance();
	}

}
