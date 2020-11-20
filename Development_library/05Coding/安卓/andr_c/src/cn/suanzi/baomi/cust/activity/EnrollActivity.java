package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.EnrollFragment;

/**
 * 报名
 * @author wensi.yu
 *
 */
public class EnrollActivity extends SingleFragmentActivity {

	private final static String TAG = EnrollActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return EnrollFragment.newInstance();
	}
	
}
