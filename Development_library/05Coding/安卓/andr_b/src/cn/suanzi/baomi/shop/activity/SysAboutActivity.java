package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ChooseStoreFragment;

/**
 *  关于苞米商家端
 * @author qian.zhou
 */
public class SysAboutActivity extends SingleFragmentActivity {
	private static final String TAG = "SysAboutActivity";
	@Override
	protected Fragment createFragment() {
		return ChooseStoreFragment.newInstance();
	}

}
