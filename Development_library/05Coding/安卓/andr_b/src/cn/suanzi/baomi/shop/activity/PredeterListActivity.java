package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.PredeterListFragment;

/**
 * 預定名單
 * @author qian.zhou
 */
public class PredeterListActivity extends SingleFragmentActivity {

	private final static String TAG = PredeterListActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return PredeterListFragment.newInstance();
	}
}
