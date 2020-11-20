package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.StoreSetFragment;

import android.app.Fragment;

/**
 * 门店设置
 * @author qian.zhou 
 */
public class StoreSetActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		return StoreSetFragment.newInstance() ;
	}
}
