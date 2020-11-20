package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.CouponVerificationFragment;

import android.app.Fragment;

/**
 * 对兑换券和代金券的验证
 * @author qian.zhou
 */
public class CouponVerificationActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return CouponVerificationFragment.newInstance();
	}
}
