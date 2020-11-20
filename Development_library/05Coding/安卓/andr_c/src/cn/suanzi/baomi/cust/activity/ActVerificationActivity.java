package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.EnrollFragment;

/**
 * 活动验证
 * @author qian.zhou
 */
public class ActVerificationActivity extends SingleFragmentActivity {

	private final static String TAG = ActVerificationActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return EnrollFragment.newInstance();
	}
}
