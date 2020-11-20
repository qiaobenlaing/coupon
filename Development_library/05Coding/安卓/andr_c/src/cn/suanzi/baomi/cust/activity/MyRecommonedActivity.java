package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.MyRecommonedFragment;

/**
 * 推荐好友
 * @author wensi.yu
 *
 */
public class MyRecommonedActivity extends SingleFragmentActivity {
	
	private static final String TAG = MyRecommonedActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return MyRecommonedFragment.newInstance();
	}
}
