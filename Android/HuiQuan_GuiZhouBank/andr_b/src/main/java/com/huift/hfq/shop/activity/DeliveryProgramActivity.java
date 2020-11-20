package com.huift.hfq.shop.activity;


import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.UpdateShopTimeFragment;

import android.app.Fragment;

/**
 * 修改商家的营业时间
 * @author qian.zhou
 */
public class DeliveryProgramActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return UpdateShopTimeFragment.newInstance();
	}
}
