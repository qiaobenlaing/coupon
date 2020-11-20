package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ShopFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 商户列表
 * 
 * @author qian.zhou
 */
public class ShopActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ShopFragment.newInstance();
	}

}
