package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActivityManagerFragment;

import android.app.Fragment;
 
/**
 * 活动管理界面
 * @author qian.zhou
 */
public class ActivityManagerActivity extends SingleFragmentActivity {
	private final static String TAG = ActivityManagerActivity.class.getSimpleName();
	@Override
	protected Fragment createFragment() {
		return ActivityManagerFragment.newInstance();
	}
}
