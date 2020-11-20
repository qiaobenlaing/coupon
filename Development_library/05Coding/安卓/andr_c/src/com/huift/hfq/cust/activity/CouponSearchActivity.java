package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.CouponSearchFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 优惠券列表
 * @author zxh
 *
 */
public class CouponSearchActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		// TODO 自动生成的方法存根
		return CouponSearchFragment.newInstance();
	}

}
