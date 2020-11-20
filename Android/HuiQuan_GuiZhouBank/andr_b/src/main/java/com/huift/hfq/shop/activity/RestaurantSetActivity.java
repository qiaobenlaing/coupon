package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.RestaurantSetFragment;

import android.app.Fragment;

/**
 * 餐厅设置
 * @author qian.zhou 
 */
public class RestaurantSetActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		return RestaurantSetFragment.newInstance() ;
	}
}
