package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActAddFragment;

import android.app.Fragment;

/**
 * @author wensi.yu
 * 添加营销活动主题，时间，地点
 */
public class ActAddActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return ActAddFragment.newInstance();
	}
}
