package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ChooseStoreFragment;

import android.app.Fragment;

/**
 * 选择店铺
 * @author qian.zhou 
 */
public class ChooseStoreActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		return ChooseStoreFragment.newInstance() ;
	}
}
