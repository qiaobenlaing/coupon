//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.CouponUseDetailFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 优惠券使用详情界面 单Fragment
 * @author yanfang.li
 */
public class CouponUseDetailActivity extends SingleFragmentActivity {
	private static final String TAG = CouponUseDetailActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return CouponUseDetailFragment.newInstance();
	}
}
