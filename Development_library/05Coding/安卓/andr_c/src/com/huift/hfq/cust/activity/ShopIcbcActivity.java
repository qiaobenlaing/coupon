package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ShopIcbcFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 工银特惠
 * @author yanfang.li
 */
public class ShopIcbcActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ShopIcbcFragment.newInstance();
	}

}
