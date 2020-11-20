package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyStoreOrTakeoutFragment;

import android.app.Fragment;
 
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
