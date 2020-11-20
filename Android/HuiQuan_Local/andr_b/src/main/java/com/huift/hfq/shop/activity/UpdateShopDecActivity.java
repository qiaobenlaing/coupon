package com.huift.hfq.shop.activity;


import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.UpdateShopDecFragment;

import android.app.Fragment;

/**
 * 修改商家的简介信息
 * @author qian.zhou
 */
public class UpdateShopDecActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return UpdateShopDecFragment.newInstance();
	}
}
