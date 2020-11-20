package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ActivityVerificationFragment;

/**
 * 活动验证
 * @author qian.zhou
 */
public class ActivityVerificationActivity extends SingleFragmentActivity {

	private final static String TAG = ActivityVerificationActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return ActivityVerificationFragment.newInstance();
	}
}
