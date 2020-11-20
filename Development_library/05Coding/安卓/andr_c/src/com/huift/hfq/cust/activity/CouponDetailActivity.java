package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.CouponDetailFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 优惠券详情界面
 * 
 * @author yanfang.li
 */
public class CouponDetailActivity extends SingleFragmentActivity {
	private static final String TAG = CouponDetailActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return CouponDetailFragment.newInstance();
	}
	
}
