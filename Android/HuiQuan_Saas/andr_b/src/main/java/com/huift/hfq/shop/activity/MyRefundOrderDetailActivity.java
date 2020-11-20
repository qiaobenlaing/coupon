package com.huift.hfq.shop.activity;
import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyRefundOrderDetailFragment;

import android.app.Fragment;
 
/**
 * 退款订单详情页面
 * @author qian.zhou
 */
public class MyRefundOrderDetailActivity extends SingleFragmentActivity {
	private static final String TAG = "MyRefundOrderDetailActivity";

	@Override
	protected Fragment createFragment() {
		return MyRefundOrderDetailFragment.newInstance();
	}
}
