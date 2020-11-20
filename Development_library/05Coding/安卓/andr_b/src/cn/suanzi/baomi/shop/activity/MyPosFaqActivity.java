package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyPosFaqFragment;

/**
 * POS FAQ
 * @author qian.zhou
 */
public class MyPosFaqActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyPosFaqFragment.newInstance();
	}
}
