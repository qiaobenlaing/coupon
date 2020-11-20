package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.BatchCouponDetailFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 同一批次优惠券详情
 * @author yanfang.li
 */
public class BatchCouponDetailActivity extends SingleFragmentActivity {
	
	private static final String TAG = BatchCouponDetailActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return BatchCouponDetailFragment.newInstance();
	}
}
