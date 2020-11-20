package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.CouponCodeUseFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;
/**
 * 优惠券兑换码使用之后界面
 * @author yingchen
 *
 */
public class CouponCodeUseActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return CouponCodeUseFragment.newInstance();
	}

}
