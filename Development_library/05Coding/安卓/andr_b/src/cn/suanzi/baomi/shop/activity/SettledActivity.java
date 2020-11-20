package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.SettledFragment;

/**
 * 我要入驻
 * @author wensi.yu
 *
 */
public class SettledActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return SettledFragment.newInstance();
	}

}
