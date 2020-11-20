package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyStaffShopListFragment;

import android.app.Fragment;

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
