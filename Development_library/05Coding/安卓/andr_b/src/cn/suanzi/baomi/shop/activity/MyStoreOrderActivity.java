package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyStoreOrTakeoutFragment;
 
/**
 * 我的订单页面
 * @author qian.zhou
 */
public class MyStoreOrderActivity extends SingleFragmentActivity {
	private static final String TAG = MyStoreOrderActivity.class.getSimpleName();
	@Override
	protected Fragment createFragment() {
		return MyStoreOrTakeoutFragment.newInstance();
	}
}
