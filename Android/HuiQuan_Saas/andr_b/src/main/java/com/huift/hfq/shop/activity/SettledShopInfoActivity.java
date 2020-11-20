package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.SettledShopInfoFragment;

import android.app.Fragment;

/**
 * 填写门店信息
 * @author wensi.yu
 */
public class SettledShopInfoActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return SettledShopInfoFragment.newInstance();
	}
	
}
