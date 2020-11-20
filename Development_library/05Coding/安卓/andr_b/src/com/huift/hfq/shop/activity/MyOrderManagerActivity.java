package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyOrderManagerFragment;

import android.app.Fragment;
 
/**
 * 我的订单页面
 * @author qian.zhou
 */
public class MyOrderManagerActivity extends SingleFragmentActivity {
	private final static String TAG = MyOrderManagerActivity.class.getSimpleName();
	@Override
	protected Fragment createFragment() {
		return MyOrderManagerFragment.newInstance();
	}
}
