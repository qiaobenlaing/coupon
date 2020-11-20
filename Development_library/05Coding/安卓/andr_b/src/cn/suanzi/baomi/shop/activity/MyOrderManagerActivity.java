package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyOrderManagerFragment;
 
/**
 * 我的订单页面
 * @author qian.zhou
 */
public class MyOrderManagerActivity extends SingleFragmentActivity {
	private final static String TAG = MyOrderManagerActivity.class.getSimpleName();
	@Override
	protected Fragment createFragment() {
		return MyOrderManagerFragment.newInstance();
	}
}
