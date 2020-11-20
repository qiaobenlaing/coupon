package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.BuyPromotionFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 购买活动
 * @author yingchen
 */
public class BuyPromotionActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		BuyPromotionFragment fragment = BuyPromotionFragment.newInstance();
		return fragment;
	}

}
