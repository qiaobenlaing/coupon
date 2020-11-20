package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyAfterOrderDetailDistributionFragment;

import android.app.Fragment;

/**
 * 订单详情餐后支付--待配送
 * @author wensi.yu
 *
 */
public class MyAfterOrderDetailDistributionActivity extends SingleFragmentActivity {

	private final static String TAG = "MyAfterOrderDetailDistributionActivity";
	
	@Override
	protected Fragment createFragment() {
		return MyAfterOrderDetailDistributionFragment.newInstance();
	}

}
