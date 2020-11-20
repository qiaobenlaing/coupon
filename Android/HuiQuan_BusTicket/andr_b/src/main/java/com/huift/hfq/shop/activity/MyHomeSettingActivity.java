package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyHomeSettingFragment;

import android.app.Fragment;

/**
 * 设置界面
 * @author qian.zhou
 */
public class MyHomeSettingActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		return MyHomeSettingFragment.newInstance();
	}
}
