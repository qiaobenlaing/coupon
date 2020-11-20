package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.TimeLimitFragment;

/**
 * 限时购
 * @author wensi.yu
 *
 */
public class TimeLimitActivity extends SingleFragmentActivity {

	private final static String TAG = TimeLimitActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return TimeLimitFragment.newInstance();
	}
}
