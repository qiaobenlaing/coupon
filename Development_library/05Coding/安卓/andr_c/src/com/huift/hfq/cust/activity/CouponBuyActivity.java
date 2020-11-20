package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.CouponBuyFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 购买优惠券
 * @author yingchen
 */
public class CouponBuyActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return CouponBuyFragment.newInstance();
	}

}
