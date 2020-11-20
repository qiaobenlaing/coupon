package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyStaffShopListFragment;

/**
 * 门店列表
 * @author wensi.yu
 *
 */
public class MyStaffShopListActivity extends SingleFragmentActivity {

	private final static String TAG = "MyStoreListActivity";
	
	@Override
	protected Fragment createFragment() {
		return MyStaffShopListFragment.newInstance();
	}
}
