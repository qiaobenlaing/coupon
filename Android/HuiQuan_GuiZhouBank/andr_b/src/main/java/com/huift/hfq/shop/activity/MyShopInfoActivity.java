package com.huift.hfq.shop.activity;


import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyShopInfoFragment;

import android.app.Fragment;

/**
 * 店铺信息显示界面
 * @author qian.zhou
 */
public class MyShopInfoActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyShopInfoFragment.newInstance();
	}
}
