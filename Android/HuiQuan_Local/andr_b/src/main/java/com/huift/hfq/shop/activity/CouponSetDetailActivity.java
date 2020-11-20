// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.CouponSetFullCutFragment;

import android.app.Fragment;

/**
 * 优惠券详细设置
 * 
 * @author yanfang.li
 */
public class CouponSetDetailActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return CouponSetFullCutFragment.newInstance();
	}
	
}
